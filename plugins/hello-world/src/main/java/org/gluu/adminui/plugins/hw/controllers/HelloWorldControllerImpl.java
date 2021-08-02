package org.gluu.adminui.plugins.hw.controllers;

import org.gluu.adminui.plugins.hw.modals.HelloWorldResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/plugins")
public class HelloWorldControllerImpl implements HelloWorldController {

    @GetMapping("/hello-world")
    public HelloWorldResponse helloWorld() {
        return new HelloWorldResponse("Hello Plugin!");
    }
}
