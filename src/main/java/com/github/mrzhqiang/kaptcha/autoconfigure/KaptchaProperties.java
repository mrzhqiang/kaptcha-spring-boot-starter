package com.github.mrzhqiang.kaptcha.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@ConfigurationProperties("spring.kaptcha")
public class KaptchaProperties {

    public static final String KEY_SESSION_CODE = "KAPTCHA_SESSION_CODE";
    public static final String KEY_SESSION_DATE = "KAPTCHA_SESSION_DATE";

    private static final String DEFAULT_PATH = "/kaptcha";
    private static final String DEFAULT_LOGIN_PATH = "/login";
    private static final String DEFAULT_FAILURE_PATH = "/login?error";
    private static final String DEFAULT_PARAMETER = "kaptcha";
    private static final String DEFAULT_EMPTY_TIPS = "验证码不能为空";
    private static final String DEFAULT_INVALID_TIPS = "无效的验证码";
    private static final String DEFAULT_TIMEOUT_TIPS = "验证码已过期";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(5);

    private Boolean enabled = false;
    private String path = DEFAULT_PATH;
    private String loginPath = DEFAULT_LOGIN_PATH;
    private String failurePath = DEFAULT_FAILURE_PATH;
    private String parameter = DEFAULT_PARAMETER;
    private String emptyTips = DEFAULT_EMPTY_TIPS;
    private String invalidTips = DEFAULT_INVALID_TIPS;
    private String timeoutTips = DEFAULT_TIMEOUT_TIPS;
    private Duration timeout = DEFAULT_TIMEOUT;
    private Properties config = new Properties();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getFailurePath() {
        return failurePath;
    }

    public void setFailurePath(String failurePath) {
        this.failurePath = failurePath;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getEmptyTips() {
        return emptyTips;
    }

    public void setEmptyTips(String emptyTips) {
        this.emptyTips = emptyTips;
    }

    public String getInvalidTips() {
        return invalidTips;
    }

    public void setInvalidTips(String invalidTips) {
        this.invalidTips = invalidTips;
    }

    public String getTimeoutTips() {
        return timeoutTips;
    }

    public void setTimeoutTips(String timeoutTips) {
        this.timeoutTips = timeoutTips;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}
