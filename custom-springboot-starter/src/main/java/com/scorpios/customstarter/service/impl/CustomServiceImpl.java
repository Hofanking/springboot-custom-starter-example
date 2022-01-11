package com.scorpios.customstarter.service.impl;

import com.scorpios.customstarter.CustomConfigurationProperties;
import com.scorpios.customstarter.service.CustomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomServiceImpl implements CustomService {

    @Autowired
    CustomConfigurationProperties customConfigurationProperties;


    @Override
    public void customMethod() {
        log.info("customMethod invoke ...");
        log.info(customConfigurationProperties.getCustomHost()+" : " + customConfigurationProperties.getCustomPort());
    }

}
