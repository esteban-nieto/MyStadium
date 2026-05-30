package com.MyStadium.Auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class AuthApplicationTest {

    @Test
    void classHasSpringBootApplication() {
        assertNotNull(AuthApplication.class.getAnnotation(SpringBootApplication.class));
    }

    @Test
    void mainMethodExists() throws Exception {
        Method main = AuthApplication.class.getMethod("main", String[].class);
        assertNotNull(main);
    }
}
