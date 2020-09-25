package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.google.code.kaptcha.util.Config;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;


/**
 * 验证码认证转换器。
 * <p>
 * 主要为了验证会话中的验证码是否有效。
 * <p>
 * 有效包括：1. 完全匹配；2. 尚未超时。
 */
public class KaptchaAuthenticationConverter implements AuthenticationConverter {

    private final Config config;
    private final KaptchaProperties properties;

    public KaptchaAuthenticationConverter(Config config, KaptchaProperties properties) {
        this.config = config;
        this.properties = properties;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!properties.getEnabled()) {
            // null is disabled or kaptcha successful
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
