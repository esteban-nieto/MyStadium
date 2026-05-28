package com.MyStadium.Catalogo.infrastructure.adapter.output.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;

@Entity
@Table(name = "conciertos")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ConciertoData {
    @Id @UuidGenerator
    private String id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String artista;
    private String estadio;
    private String ciudad;
    private LocalDate fecha;
    private Double precioBase;
    private String imagenUrl;
}
