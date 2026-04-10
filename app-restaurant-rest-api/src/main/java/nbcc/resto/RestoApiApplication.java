package nbcc.resto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"nbcc.resto", "nbcc.common", "nbcc.auth", "nbcc.email"})
public class RestoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestoApiApplication.class, args);
    }
}
