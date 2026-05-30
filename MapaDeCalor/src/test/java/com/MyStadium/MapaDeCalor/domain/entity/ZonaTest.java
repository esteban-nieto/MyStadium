package com.MyStadium.MapaDeCalor.domain.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ZonaTest {
    @Test
    void getColor_Rojo() {
        Zona z = Zona.builder().capacidad(100).ocupados(80).build();
        assertEquals("ROJO", z.getColor());
    }

    @Test
    void getColor_Amarillo() {
        Zona z = Zona.builder().capacidad(100).ocupados(40).build();
        assertEquals("AMARILLO", z.getColor());
    }

    @Test
    void getColor_Azul() {
        Zona z = Zona.builder().capacidad(100).ocupados(10).build();
        assertEquals("AZUL", z.getColor());
    }

    @Test
    void getDisponibles_CalculaCorrecto() {
        Zona z = Zona.builder().capacidad(100).ocupados(30).build();
        assertEquals(70, z.getDisponibles());
    }

    @Test
    void getDisponibles_Cero() {
        Zona z = Zona.builder().capacidad(100).ocupados(100).build();
        assertEquals(0, z.getDisponibles());
    }

    @Test
    void getColor_BoundaryRojo() {
        Zona z = Zona.builder().capacidad(100).ocupados(71).build();
        assertEquals("ROJO", z.getColor());
    }

    @Test
    void getColor_BoundaryAmarilloSuperior() {
        Zona z = Zona.builder().capacidad(100).ocupados(70).build();
        assertEquals("AMARILLO", z.getColor());
    }

    @Test
    void getColor_BoundaryAmarilloInferior() {
        Zona z = Zona.builder().capacidad(100).ocupados(30).build();
        assertEquals("AMARILLO", z.getColor());
    }

    @Test
    void getColor_BoundaryAzul() {
        Zona z = Zona.builder().capacidad(100).ocupados(29).build();
        assertEquals("AZUL", z.getColor());
    }

    @Test
    void getColor_CapacidadCero() {
        Zona z = Zona.builder().capacidad(0).ocupados(0).build();
        assertEquals("AZUL", z.getColor());
    }

    @Test
    void getDisponibles_Negativos() {
        Zona z = Zona.builder().capacidad(100).ocupados(150).build();
        assertEquals(-50, z.getDisponibles());
    }
}
