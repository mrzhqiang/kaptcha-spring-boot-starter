package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.github.mrzhqiang.helper.captcha.Captcha;
import com.github.mrzhqiang.helper.captcha.simple.SimpleCaptcha;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

/**
 * 验证码自动配置类。
 * <p>
 * 配置注解 {@link Configuration} 的 proxyBeanMethods 属性，false 表示自调用方法不能被代理。
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(KaptchaProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Captcha.class, SimpleCaptcha.class})
public class KaptchaAutoConfiguration {

    private final KaptchaProperties properties;

    public KaptchaAutoConfiguration(KaptchaProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public Captcha captcha() {
        return SimpleCaptcha.of();
    }

    @Bean
    @ConditionalOnMissingBean
    public KaptchaController controller(Captcha captcha) {
        return new KaptchaController(captcha, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public BufferedImageHttpMessageConverter messageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public KaptchaAuthenticationConverter authenticationConverter() {
        return new KaptchaAuthenticationConverter(properties);
    }
}
