package com.scorpios.customenhance.autoconfigure;


import com.scorpios.customenhance.config.CustomEnhanceRegister;
import com.scorpios.customenhance.service.CustomEnhanceService;
import com.scorpios.customenhance.service.impl.CustomEnhanceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
//@EnableConfigurationProperties(CustomConfigurationProperties.class)
@Import(CustomEnhanceRegister.class)
@ConditionalOnClass(CustomEnhanceService.class) // 当类路径下有指定类 改配置才有效
public class CustomEnhanceAutoConfiguration {


    @Bean
    public CustomEnhanceService customEnhanceService(){
        log.info("CustomEnhanceService create.....");
        return  new CustomEnhanceServiceImpl();
    }

}
