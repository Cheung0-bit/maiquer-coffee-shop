package tech.maiquer.common.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("metric.sms")
public class SmsProperty {

    private String accessKeyId;

    private String accessKeySecret;

    private String domain;

    private String signName;

    private String templateCode;

}
