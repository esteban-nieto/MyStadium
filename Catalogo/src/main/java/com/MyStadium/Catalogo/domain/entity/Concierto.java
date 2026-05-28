package com.MyStadium.Catalogo.domain.entity;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Concierto {
    private String id;
    private String nombre;
    private String artista;
    private String estadio;
    private String ciudad;
    private LocalDate fecha;
    private Double precioBase;
    private String imagenUrl;
}
