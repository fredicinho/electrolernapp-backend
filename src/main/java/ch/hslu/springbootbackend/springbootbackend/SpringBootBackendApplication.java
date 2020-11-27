package ch.hslu.springbootbackend.springbootbackend;

import ch.hslu.springbootbackend.springbootbackend.controllers.CategoryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EntityScan(basePackages = { "ch.hslu.springbootbackend.springbootbackend.Entity" })
public class SpringBootBackendApplication {
    private final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC+1"));   // It will set UTC timezone
        System.out.println("Spring boot application running in UTC timezone :"+new Date());   // It will print UTC timezone
    }
    public static void main(String[] args) {
        SpringApplication.run(SpringBootBackendApplication.class, args);
    }
    
}
