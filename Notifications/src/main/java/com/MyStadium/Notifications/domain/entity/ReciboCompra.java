package com.MyStadium.Notifications.domain.entity;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReciboCompra {
    private String id;
    private String usuarioId;
    private String emailUsuario;
    private String codigoOrden;
    private String nombreConcierto;
    private List<String> asientos;
    private Double totalPagado;
    private LocalDateTime fechaPago;
    private LocalDateTime fechaEnvio;
}
