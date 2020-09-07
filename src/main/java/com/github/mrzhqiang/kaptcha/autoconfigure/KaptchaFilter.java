package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.google.code.kaptcha.util.Config;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class KaptchaFilter extends AbstractAuthenticationProcessingFilter {

    private final Config config;
    private final KaptchaProperties properties;

    public KaptchaFilter(Config config, KaptchaProperties properties) {
        super(new AntPathRequestMatcher(properties.getPath(), HttpMethod.POST.name()));
        this.config = config;
        this.properties = properties;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(properties.getFailurePath()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!properties.getEnabled()) {
            return null;
        }
        String verifyCode = request.getParameter(properties.getParameter());
        if (StringUtils.isEmpty(verifyCode)) {
            throw new AuthenticationCredentialsNotFoundException(properties.getEmptyTips());
        }
        HttpSession session = request.getSession();
        String code = String.valueOf(session.getAttribute(config.getSessionKey()));
        if (StringUtils.isEmpty(code) || !Objects.equals(verifyCode, code)) {
            throw new BadCredentialsException(properties.getInvalidTips());
        }
        Object date = session.getAttribute(config.getSessionDate());
        if (date instanceof Date) {
            Date verifyDate = (Date) date;
            Duration between = Duration.between(verifyDate.toInstant(), Instant.now());
            if (properties.getTimeout().minus(between).isNegative()) {
                throw new CredentialsExpiredException(properties.getTimeoutTips());
            }
        }
        return null;
    }
}
