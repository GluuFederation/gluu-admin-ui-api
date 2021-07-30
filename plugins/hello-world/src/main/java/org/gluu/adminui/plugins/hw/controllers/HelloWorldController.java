package org.gluu.adminui.plugins.hw.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/plugins")
public class HelloWorldController {

    @GetMapping("/hello-world")
    public String helloWorld() throws URISyntaxException {
        return "Hello Plugin!";
    }
}
