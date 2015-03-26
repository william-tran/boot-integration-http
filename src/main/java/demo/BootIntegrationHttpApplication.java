package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"echo-config.xml", "soap-config.xml"})
public class BootIntegrationHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootIntegrationHttpApplication.class, args);
    }
}
