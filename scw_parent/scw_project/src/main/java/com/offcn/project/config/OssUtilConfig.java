package com.offcn.project.config;

import com.offcn.utils.OssUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssUtilConfig {

    @ConfigurationProperties(prefix = "oss")
    @Bean
    public OssUtil inst() {
        return new OssUtil();
    }

}
