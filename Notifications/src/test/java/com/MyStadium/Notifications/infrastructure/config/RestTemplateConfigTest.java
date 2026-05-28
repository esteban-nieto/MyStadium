package com.MyStadium.Notifications.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

class RestTemplateConfigTest {
    @Test
    void restTemplate_BeanCreado() {
        RestTemplateConfig config = new RestTemplateConfig();
        RestTemplate template = config.restTemplate();
        assertNotNull(template);
    }
}
