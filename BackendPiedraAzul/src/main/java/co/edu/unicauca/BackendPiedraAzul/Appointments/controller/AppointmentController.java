package co.edu.unicauca.BackendPiedraAzul.Appointments.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/appointments")
public class AppointmentController{

    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

}
