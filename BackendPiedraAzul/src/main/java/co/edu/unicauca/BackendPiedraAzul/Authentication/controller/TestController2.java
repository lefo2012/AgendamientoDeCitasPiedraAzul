package co.edu.unicauca.BackendPiedraAzul.Authentication.controller;

import co.edu.unicauca.BackendPiedraAzul.Users.validation.LoginValidation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test2")
public class TestController2 {

    private final LoginValidation service;

    public TestController2(LoginValidation service) {
        this.service = service;
    }

    @GetMapping("/test-login-flow")
    public String test() {

        service.testLoginFlow();

        return "Revisa la consola";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }



}