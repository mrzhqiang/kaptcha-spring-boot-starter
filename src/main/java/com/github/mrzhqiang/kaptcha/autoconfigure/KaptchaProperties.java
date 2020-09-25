package com.github.mrzhqiang.kaptcha.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * 验证码配置。
 */
@ConfigurationProperties("spring.kaptcha")
@Validated
public class KaptchaProperties {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(5);
    private static final String DEFAULT_EMPTY_TIPS = "验证码不能为空";
    private static final String DEFAULT_INVALID_TIPS = "无效的验证码";
    private static final String DEFAULT_TIMEOUT_TIPS = "验证码已过期";
    private static final String DEFAULT_PATH = "/kaptcha";
    private static final String DEFAULT_PARAMETER = "kaptcha";

    @NotNull
    private Boolean enabled = false;
    @NotEmpty
    private String path = DEFAULT_PATH;
    @NotEmpty
    private String parameter = DEFAULT_PARAMETER;
    @NotEmpty
    private String emptyTips = DEFAULT_EMPTY_TIPS;
    @NotEmpty
    private String invalidTips = DEFAULT_INVALID_TIPS;
    @NotEmpty
    private String timeoutTips = DEFAULT_TIMEOUT_TIPS;
    @NotNull
    private Duration timeout = DEFAULT_TIMEOUT;
    @NotNull
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

    @Override
    public String toString() {
        return new StringJoiner(", ", KaptchaProperties.class.getSimpleName() + "[", "]")
                .add("enabled=" + enabled)
                .add("path='" + path + "'")
                .add("parameter='" + parameter + "'")
                .add("emptyTips='" + emptyTips + "'")
                .add("invalidTips='" + invalidTips + "'")
                .add("timeoutTips='" + timeoutTips + "'")
                .add("timeout=" + timeout)
                .add("config=" + config)
                .toString();
    }
}
