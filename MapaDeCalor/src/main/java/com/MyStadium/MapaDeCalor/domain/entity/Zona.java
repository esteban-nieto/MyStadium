package com.MyStadium.MapaDeCalor.domain.entity;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Zona {
    private String id;
    private String conciertoId;
    private String nombre;
    private int capacidad;
    private int ocupados;
    private double precio;

    public String getColor() {
        double ratio = (double) ocupados / capacidad;
        if (ratio > 0.7) return "ROJO";
        if (ratio >= 0.3) return "AMARILLO";
        return "AZUL";
    }

    public int getDisponibles() {
        return capacidad - ocupados;
    }
}
