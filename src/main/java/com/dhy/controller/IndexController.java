package com.dhy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/koala")
    public String indexStart(){
        return "backstage";
    }

}

