package com.MyStadium.Notifications;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class NotificationsApplicationTest {

    @Test
    void classHasSpringBootApplication() {
        assertNotNull(NotificationsApplication.class.getAnnotation(SpringBootApplication.class));
    }

    @Test
    void mainMethodExists() throws Exception {
        Method main = NotificationsApplication.class.getMethod("main", String[].class);
        assertNotNull(main);
    }
}
