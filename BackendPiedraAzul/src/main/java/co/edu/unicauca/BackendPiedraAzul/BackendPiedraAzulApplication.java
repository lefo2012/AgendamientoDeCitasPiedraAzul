package co.edu.unicauca.BackendPiedraAzul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"Authentication", "co.edu.unicauca.BackendPiedraAzul"})
public class BackendPiedraAzulApplication {

	public static void main(String[] args) {
        SpringApplication.run(BackendPiedraAzulApplication.class, args);
    }
}
