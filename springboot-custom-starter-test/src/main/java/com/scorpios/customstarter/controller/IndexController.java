package com.scorpios.customstarter.controller;

import com.scorpios.customstarter.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    CustomService customService;

    @RequestMapping("/index")
    public String index(){
        customService.customMethod();
        return "index 8001...";
    }

}
