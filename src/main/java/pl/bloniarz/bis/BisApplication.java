package pl.bloniarz.bis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BisApplication.class, args);
    }

}
