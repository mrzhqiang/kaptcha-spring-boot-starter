package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.google.code.kaptcha.util.Config;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class KaptchaSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final Config config;
    private final KaptchaProperties properties;

    public KaptchaSecurityConfigurerAdapter(Config config, KaptchaProperties properties) {
        this.config = config;
        this.properties = properties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(registry ->
                registry.antMatchers(properties.getPath()).permitAll()
                        .antMatchers(properties.getIgnorePath()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(configurer -> configurer.loginPage(properties.getLoginPath()).permitAll())
                .logout().permitAll();

        KaptchaFilter verifyCodeFilter = new KaptchaFilter(config, properties);
        verifyCodeFilter.setAuthenticationManager(authenticationManager());
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
