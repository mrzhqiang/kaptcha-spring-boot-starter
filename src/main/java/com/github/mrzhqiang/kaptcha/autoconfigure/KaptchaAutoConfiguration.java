package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 验证码自动配置类。
 * <p>
 * 配置注解 {@link Configuration} 的 proxyBeanMethods 属性，false 表示自调用方法不能被代理。
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KaptchaProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(Config.class)
public class KaptchaAutoConfiguration {

    private static final String LOGIN_PATH = "/login";
    private static final String LOGIN_METHOD = "POST";

    private final KaptchaProperties properties;

    public KaptchaAutoConfiguration(KaptchaProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Config config() {
        return new Config(properties.getConfig());
    }

    @Bean
    @ConditionalOnMissingBean
    public KaptchaController controller(Config config) {
        return new KaptchaController(config, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedImageHttpMessageConverter messageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public KaptchaAuthenticationConverter authenticationConverter(Config config) {
        return new KaptchaAuthenticationConverter(config, properties);
    }

    @Bean
    @ConditionalOnClass(WebSecurityConfigurerAdapter.class)
    @ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public WebSecurityConfigurerAdapter adapter(KaptchaAuthenticationConverter converter) {
        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), converter);
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                        .authorizeRequests()
                        .antMatchers(properties.getPath()).permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin().loginPage(LOGIN_PATH).permitAll()
                        .and()
                        .logout().permitAll();
            }
        };
    }
}
