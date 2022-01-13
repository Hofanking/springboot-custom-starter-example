package com.scorpios.customenhance.service.impl;

import com.scorpios.customenhance.annotation.Scorpios;
import com.scorpios.customenhance.service.CustomEnhanceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Scorpios
public class CustomEnhanceServiceImpl implements CustomEnhanceService {

    @Override
    public void enhance() {
        log.info("CustomEnhanceServiceImpl...enhance.....");
    }
}
