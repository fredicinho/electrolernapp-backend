package ch.hslu.springbootbackend.springbootbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringBootBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void springVersion(){
        assertEquals("5.2.9.RELEASE", SpringVersion.getVersion());

    }

    @Test
    void getJavaVersion(){
        assertEquals("11.0.2", System.getProperty("java.version"));

    }
}
