package com.MyStadium.MapaDeCalor.domain.entity;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Boleto {
    private String id;
    private String codigoUnico;
    private String usuarioId;
    private String conciertoId;
    private String conciertoNombre;
    private String artista;
    private String zonaNombre;
    private double zonaPrecio;
    private String asiento;
    private double totalPagado;
    private LocalDateTime fechaCompra;
    private String estadio;
    private LocalDateTime fechaEvento;
    private String codigoOrden;
    private TicketType tipoEntrada;
}
