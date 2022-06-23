package com.github.mrzhqiang.kaptcha.autoconfigure;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    private final KaptchaProperties properties;

    public KaptchaAuthenticationConverter(KaptchaProperties properties) {
        this.properties = properties;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!properties.getEnabled()) {
            // 禁用验证码，返回 null 不做任何转换，继续下一步操作
            return null;
        }

        String verifyCode = request.getParameter(properties.getParameter());
        if (!StringUtils.hasText(verifyCode)) {
            throw new AuthenticationCredentialsNotFoundException(properties.getEmptyTips());
        }

        HttpSession session = request.getSession();
        String code = String.valueOf(session.getAttribute(KaptchaProperties.KEY_SESSION_CODE));
        if (!StringUtils.hasText(code) || !Objects.equals(verifyCode, code)) {
            throw new BadCredentialsException(properties.getInvalidTips());
        }
        Object date = session.getAttribute(KaptchaProperties.KEY_SESSION_DATE);
        if (date instanceof Date) {
            Instant timeoutInstant = ((Date) date).toInstant().plus(properties.getTimeout());
            if (Instant.now().isAfter(timeoutInstant)) {
                throw new CredentialsExpiredException(properties.getTimeoutTips());
            }
        }
        return null;
    }
}
