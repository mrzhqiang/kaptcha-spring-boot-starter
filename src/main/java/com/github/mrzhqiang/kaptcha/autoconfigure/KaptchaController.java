package com.github.mrzhqiang.kaptcha.autoconfigure;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
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
    private final Config config;
    private final KaptchaProperties properties;

    public KaptchaController(Config config, KaptchaProperties properties) {
        this.config = config;
        this.properties = properties;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<BufferedImage> index(HttpSession session) {
        if (!properties.getEnabled()) {
            return ResponseEntity.noContent().build();
        }
        Producer producer = config.getProducerImpl();
        String code = producer.createText();
        session.setAttribute(config.getSessionKey(), code);
        session.setAttribute(config.getSessionDate(), new Date());

        BufferedImage image = producer.createImage(code);
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
