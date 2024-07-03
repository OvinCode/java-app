package com.example.dataparse.assignment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    public static void getWorld(){
        System.out.println("Hello World My Name ");
    }


}
