package com.MyStadium.Notifications.infrastructure.config;

import com.MyStadium.Notifications.domain.gateway.EnvioEmailGateway;
import com.MyStadium.Notifications.domain.gateway.EnvioPushGateway;
import com.MyStadium.Notifications.domain.usecase.NotificacionUseCase;
import com.MyStadium.Notifications.pdf.BannerCacheService;
import com.MyStadium.Notifications.pdf.TicketPdfGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UseCaseConfig {
    @Value("${server.mapa-calor.url}")
    private String mapaCalorUrl;

    @Value("${server.catalogo.url}")
    private String catalogoUrl;

    @Value("${app.images.base-path}")
    private String imagesBasePath;

    @Bean
    public NotificacionUseCase notificacionUseCase(
            EnvioEmailGateway envioEmailGateway, EnvioPushGateway envioPushGateway,
            JavaMailSender mailSender, RestTemplate restTemplate,
            TicketPdfGenerator ticketPdfGenerator) {
        return new NotificacionUseCase(envioEmailGateway, envioPushGateway,
                mailSender, restTemplate, mapaCalorUrl, catalogoUrl,
                ticketPdfGenerator, imagesBasePath);
    }
}
