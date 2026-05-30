package com.MyStadium.MapaDeCalor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class MapaDeCalorApplicationTest {

    @Test
    void classHasSpringBootApplication() {
        assertNotNull(MapaDeCalorApplication.class.getAnnotation(SpringBootApplication.class));
    }

    @Test
    void mainMethodExists() throws Exception {
        Method main = MapaDeCalorApplication.class.getMethod("main", String[].class);
        assertNotNull(main);
    }
}
