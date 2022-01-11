package com.scorpios.customstarter.config;


import com.scorpios.customstarter.CustomConfigurationProperties;
import com.scorpios.customstarter.service.CustomService;
import com.scorpios.customstarter.service.impl.CustomServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(CustomConfigurationProperties.class)
@ConditionalOnClass(CustomService.class) // 当类路径下有指定类 改配置才有效
public class CustomConfigurationClass {

//    @ConditionalOnMissingBean(CustomService.class)
    @Bean
    public CustomService customService(){
        log.info("customService....");
        return new CustomServiceImpl();
    }

}
