package Authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController2 {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}