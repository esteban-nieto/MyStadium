package com.MyStadium.Catalogo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class CatalogoApplicationTest {

    @Test
    void classHasSpringBootApplication() {
        assertNotNull(CatalogoApplication.class.getAnnotation(SpringBootApplication.class));
    }

    @Test
    void mainMethodExists() throws Exception {
        Method main = CatalogoApplication.class.getMethod("main", String[].class);
        assertNotNull(main);
    }
}
