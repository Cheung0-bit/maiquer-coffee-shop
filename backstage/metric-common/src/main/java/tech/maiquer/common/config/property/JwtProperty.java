package tech.maiquer.common.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties("metric.jwt")
public class JwtProperty {

    /**
     * 密钥KEY
     */
    public static String secret;
    /**
     * TokenKey
     */
    public static String tokenHeader;
    /**
     * Token前缀字符
     */
    public static String tokenPrefix;
    /**
     * 过期时间
     */
    public static Integer expiration;

    public void setSecret(String secret) {
        JwtProperty.secret = secret;
    }

    public void setTokenHeader(String tokenHeader) {
        JwtProperty.tokenHeader = tokenHeader;
    }

    public void setTokenPrefix(String tokenPrefix) {
        JwtProperty.tokenPrefix = tokenPrefix;
    }

    public void setExpiration(Integer expiration) {
        JwtProperty.expiration = expiration;
    }
}
