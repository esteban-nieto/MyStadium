package com.MyStadium.Notifications.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Notificacion {
    private String id;
    private String usuarioId;
    private String tipo;
    private String mensaje;
    private String estado;
    private LocalDateTime fechaCreacion;
    private byte[] pdfBytes;
    private String codigoBoleto;
}
