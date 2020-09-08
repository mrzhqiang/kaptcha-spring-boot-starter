package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KaptchaProperties.class)
@ConditionalOnWebApplication
@ConditionalOnClass(Config.class)
public class KaptchaAutoConfiguration {

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
    @ConditionalOnClass(WebSecurityConfigurerAdapter.class)
    @ConditionalOnMissingBean
    public KaptchaSecurityConfigurerAdapter adapter(Config config) {
        return new KaptchaSecurityConfigurerAdapter(config, properties);
    }
}
