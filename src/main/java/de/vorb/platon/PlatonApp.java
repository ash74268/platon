package de.vorb.platon;

import de.vorb.platon.security.HmacRequestVerifier;
import de.vorb.platon.security.SecretKeyProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class PlatonApp {

    private static final Logger logger = LoggerFactory.getLogger(PlatonApp.class);

    public static void main(String... args) {
        SpringApplication.run(PlatonApp.class, args);
    }

    private final SecretKey secretKey;

    public PlatonApp() throws NoSuchAlgorithmException {
        secretKey = KeyGenerator.getInstance(HmacRequestVerifier.MAC_ALGORITHM).generateKey();

        logger.info("Secret key: {}", Base64.getEncoder().encodeToString(secretKey.getEncoded()));
    }

    @Bean
    public WebMvcConfigurerAdapter staticResourceHandlerConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("classpath:/static");
            }
        };
    }

    @Bean
    public SecretKeyProvider secretKeyProvider() {
        return () -> secretKey;
    }

}
