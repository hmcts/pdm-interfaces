package uk.gov.hmcts;

import jakarta.ws.rs.ApplicationPath;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import uk.gov.hmcts.config.WebAppInitializer;

@SpringBootApplication
@EntityScan
@EnableAutoConfiguration
@ApplicationPath("/pdm")
public class PdmanagerSpringbootApplication {

    protected PdmanagerSpringbootApplication() {
        /*
         * empty constructor
         */
    }

    public static void main(String[] args) {
        SpringApplication.run(
            new Class[] {PdmanagerSpringbootApplication.class, WebAppInitializer.class}, args);
    }
}
