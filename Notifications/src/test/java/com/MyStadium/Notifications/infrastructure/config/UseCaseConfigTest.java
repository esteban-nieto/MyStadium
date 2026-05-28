package com.MyStadium.Notifications.infrastructure.config;

import com.MyStadium.Notifications.domain.gateway.EnvioEmailGateway;
import com.MyStadium.Notifications.domain.gateway.EnvioPushGateway;
import com.MyStadium.Notifications.domain.usecase.NotificacionUseCase;
import com.MyStadium.Notifications.pdf.TicketPdfGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {
    @Mock private EnvioEmailGateway envioEmailGateway;
    @Mock private EnvioPushGateway envioPushGateway;
    @Mock private JavaMailSender mailSender;
    @Mock private RestTemplate restTemplate;
    @Mock private TicketPdfGenerator ticketPdfGenerator;

    @Test
    void notificacionUseCase_BeanCreado() {
        UseCaseConfig config = new UseCaseConfig();
        NotificacionUseCase useCase = config.notificacionUseCase(
                envioEmailGateway, envioPushGateway, mailSender, restTemplate,
                ticketPdfGenerator);
        assertNotNull(useCase);
    }
}
