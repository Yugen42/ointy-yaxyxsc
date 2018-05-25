package us.yugen.yaxyxsc;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Andreas Hartmann
 */
@EnableAutoConfiguration
@SpringBootApplication
public class Main {
    public static void main(final String[] args) {
        MockData.mockData();

        final SpringApplication mainController = new SpringApplication(MainController.class);
        mainController.setBannerMode(Banner.Mode.OFF);
        mainController.run(args);
    }
}