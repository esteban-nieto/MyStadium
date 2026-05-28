package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "zonas")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ZonaData {
    @Id @UuidGenerator
    private String id;
    @Column(nullable = false)
    private String conciertoId;
    @Column(nullable = false)
    private String nombre;
    private int capacidad;
    private int ocupados;
    private double precio;
}
