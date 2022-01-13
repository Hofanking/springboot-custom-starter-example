package com.scorpios.customstarter.controller;

import com.scorpios.customenhance.service.CustomEnhanceService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Autowired
    CustomEnhanceService customEnhanceService;

    @RequestMapping("/index")
    public String index(){
        customEnhanceService.enhance();
        return "index 8001...";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
