package com.cz.batch.lottery.global.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "dh.user")
public class DhUserDto {
    private String id;
    private String pw;
}
