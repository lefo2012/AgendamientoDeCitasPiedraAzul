package Authentication.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello-1")
    public String hello()
    {
        return "hello";
    }

    @GetMapping("/hello-2")
    public String hello2()
    {
        return "hello pero 2";
    }
}
