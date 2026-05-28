package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "boletos")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BoletoData {
    @Id @UuidGenerator
    private String id;
    @Column(unique = true, nullable = false)
    private String codigoUnico;
    @Column(nullable = false)
    private String usuarioId;
    @Column(nullable = false)
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
    private String tipoEntrada;
}
