package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.github.mrzhqiang.helper.captcha.Captcha;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Date;

@Controller
@RequestMapping("${spring.kaptcha.path:/kaptcha}")
public class KaptchaController {
    private final Captcha captcha;
    private final KaptchaProperties properties;

    public KaptchaController(Captcha captcha, KaptchaProperties properties) {
        this.captcha = captcha;
        this.properties = properties;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<BufferedImage> index(HttpSession session) {
        if (!properties.getEnabled()) {
            return ResponseEntity.noContent().build();
        }

        String code = captcha.text();

        session.setAttribute(KaptchaProperties.KEY_SESSION_CODE, code);
        session.setAttribute(KaptchaProperties.KEY_SESSION_DATE, new Date());

        BufferedImage image = captcha.image(code);
        HttpHeaders headers = new HttpHeaders();
        // Set to expire far in the past
        headers.setExpires(0);
        // Set standard HTTP/1.1 no-cache headers.
        headers.setCacheControl(CacheControl.noStore().mustRevalidate());
        // Set standard HTTP/1.0 no-cache header.
        headers.setPragma("no-cache");
        // return a jpeg
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
                .header(HttpHeaders.CACHE_CONTROL, "post-check=0", "pre-check=0")
                .body(image);
    }
}
