package com.MyStadium.Notifications.domain.usecase;

import com.MyStadium.Notifications.domain.entity.Notificacion;
import com.MyStadium.Notifications.domain.entity.ReciboCompra;
import com.MyStadium.Notifications.domain.gateway.EnvioEmailGateway;
import com.MyStadium.Notifications.domain.gateway.EnvioPushGateway;
import com.MyStadium.Notifications.pdf.TicketPdfGenerator;
import com.MyStadium.Notifications.pdf.dto.TicketPdfData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacionUseCaseTest {
    @Mock private EnvioEmailGateway envioEmailGateway;
    @Mock private EnvioPushGateway envioPushGateway;
    @Mock private JavaMailSender mailSender;
    @Mock private RestTemplate restTemplate;
    @Mock private TicketPdfGenerator ticketPdfGenerator;
    @Captor private ArgumentCaptor<TicketPdfData> pdfDataCaptor;
    private NotificacionUseCase notificacionUseCase;
    private ReciboCompra recibo;

    @BeforeEach
    void setUp() {
        notificacionUseCase = new NotificacionUseCase(envioEmailGateway, envioPushGateway,
                mailSender, restTemplate, "http://localhost:8084", "http://localhost:8082",
                ticketPdfGenerator, "src/test/resources");
        recibo = ReciboCompra.builder().usuarioId("user-123").emailUsuario("test@test.com")
                .codigoOrden("ORD-001").nombreConcierto("Concierto Test")
                .asientos(List.of("A1", "A2")).totalPagado(150000.0).fechaPago(LocalDateTime.now()).build();
    }

    @Test
    void enviarEmailVerificacion_Exito() {
        when(envioEmailGateway.enviarVerificacionEmail(anyString(), anyString())).thenReturn(true);
        Notificacion resultado = notificacionUseCase.enviarEmailVerificacion("test@test.com", "user-123");
        assertNotNull(resultado); assertEquals("VERIFICATION", resultado.getTipo());
        assertEquals("SENT", resultado.getEstado()); assertEquals("user-123", resultado.getUsuarioId());
        verify(envioEmailGateway, times(1)).enviarVerificacionEmail("test@test.com", "user-123");
    }

    @Test void enviarEmailVerificacion_FallaPorEmailNulo() {
        assertThrows(IllegalArgumentException.class, () -> notificacionUseCase.enviarEmailVerificacion(null, "user-123"));
    }

    @Test void enviarEmailVerificacion_FallaPorUsuarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> notificacionUseCase.enviarEmailVerificacion("test@test.com", null));
    }

    @Test void enviarEmailRecuperacion_Exito() {
        when(envioEmailGateway.enviarRecuperacionEmail(anyString(), anyString())).thenReturn(true);
        Notificacion resultado = notificacionUseCase.enviarEmailRecuperacion("test@test.com", "http://reset");
        assertNotNull(resultado); assertEquals("PASSWORD_RECOVERY", resultado.getTipo());
        assertEquals("SENT", resultado.getEstado());
        verify(envioEmailGateway, times(1)).enviarRecuperacionEmail("test@test.com", "http://reset");
    }

    @Test void enviarEmailRecuperacion_FallaPorEmailNulo() {
        assertThrows(IllegalArgumentException.class, () -> notificacionUseCase.enviarEmailRecuperacion(null, "link"));
    }

    @Test void enviarReciboCompra_Exito() {
        when(envioEmailGateway.enviarReciboCompra(any(ReciboCompra.class))).thenReturn(true);
        ReciboCompra resultado = notificacionUseCase.enviarReciboCompra(recibo);
        assertNotNull(resultado); assertNotNull(resultado.getFechaEnvio()); assertEquals("ORD-001", resultado.getCodigoOrden());
        verify(envioEmailGateway, times(1)).enviarReciboCompra(recibo);
        verify(envioPushGateway, times(1)).enviarPush(anyString(), anyString(), anyString());
    }

    @Test void enviarReciboCompra_FallaPorEmailNulo() {
        recibo.setEmailUsuario(null);
        assertThrows(IllegalArgumentException.class, () -> notificacionUseCase.enviarReciboCompra(recibo));
    }

    @Test void enviarReciboCompra_FallaPorCodigoOrdenNulo() {
        recibo.setCodigoOrden(null);
        assertThrows(IllegalArgumentException.class, () -> notificacionUseCase.enviarReciboCompra(recibo));
    }

    @Test void enviarReciboConPdf_Exito() throws Exception {
        byte[] fakePdf = "PDF-CONTENT".getBytes();
        when(ticketPdfGenerator.generate(any())).thenReturn(fakePdf);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(restTemplate.getForObject(contains("/boleto/"), eq(Map.class)))
                .thenReturn(Map.of("conciertoNombre","Concierto","artista","Artista",
                        "zonaNombre","VIP","asiento","A1","totalPagado",150000.0,"codigoUnico","MST-ABC123"));
        Map<String, Object> resultado = notificacionUseCase.enviarReciboConPdf(
                List.of("MST-ABC123"), "user-123", "test@test.com");
        assertNotNull(resultado);
        assertTrue(resultado.containsKey("mensaje"));
        verify(mailSender, times(1)).send(mimeMessage);
        verify(ticketPdfGenerator, times(1)).generate(any());
    }

    @Test void enviarReciboConPdf_MultiplesBoletos() throws Exception {
        byte[] fakePdf = "PDF-CONTENT".getBytes();
        when(ticketPdfGenerator.generate(any())).thenReturn(fakePdf);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(restTemplate.getForObject(contains("/boleto/"), eq(Map.class)))
                .thenReturn(Map.of("conciertoNombre","Concierto","artista","Artista",
                        "zonaNombre","VIP","asiento","A1","totalPagado",150000.0,"codigoUnico","MST-001"),
                        Map.of("conciertoNombre","Concierto","artista","Artista",
                                "zonaNombre","VIP","asiento","A2","totalPagado",150000.0,"codigoUnico","MST-002"));
        Map<String, Object> resultado = notificacionUseCase.enviarReciboConPdf(
                List.of("MST-001", "MST-002"), "user-123", "test@test.com");
        assertNotNull(resultado);
        verify(mailSender, times(1)).send(mimeMessage);
        verify(ticketPdfGenerator, times(1)).generate(any());
    }

    @Test void enviarReciboConPdf_CodigosVacios() {
        Map<String, Object> resultado = notificacionUseCase.enviarReciboConPdf(List.of(), "user-123", "test@test.com");
        assertTrue(resultado.containsKey("error"));
    }

    @Test void obtenerPdf_DesdeCache() throws Exception {
        byte[] fakePdf = "PDF-CONTENT".getBytes();
        when(ticketPdfGenerator.generate(any())).thenReturn(fakePdf);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(
                Map.of("conciertoNombre","C","artista","A","zonaNombre","Z",
                        "asiento","S","totalPagado",100.0,"codigoUnico","MST-X"));
        notificacionUseCase.enviarReciboConPdf(List.of("MST-X"), "u1", "e@e.com");
        byte[] pdf = notificacionUseCase.obtenerPdf("MST-X");
        assertNotNull(pdf);
    }

    @Test void obtenerPdf_DesdeApi() {
        byte[] fakePdf = "PDF-CONTENT".getBytes();
        when(ticketPdfGenerator.generate(any())).thenReturn(fakePdf);
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(Map.of("conciertoNombre","C","artista","A","zonaNombre","Z",
                        "asiento","S","totalPagado",100.0,"codigoUnico","MST-NEW"));
        byte[] pdf = notificacionUseCase.obtenerPdf("MST-NEW");
        assertNotNull(pdf);
    }

    @Test void obtenerPdf_NoEncontrado() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(null);
        assertNull(notificacionUseCase.obtenerPdf("MST-NOEXISTE"));
    }

    @Test
    void obtenerHistorialNotificaciones_Exito() {
        when(envioEmailGateway.enviarVerificacionEmail(anyString(), anyString())).thenReturn(true);
        notificacionUseCase.enviarEmailVerificacion("test@test.com", "user-123");
        assertFalse(notificacionUseCase.obtenerHistorialNotificaciones("user-123").isEmpty());
    }

    @Test void obtenerHistorialNotificaciones_Vacio() {
        assertTrue(notificacionUseCase.obtenerHistorialNotificaciones("user-unknown").isEmpty());
    }
}
