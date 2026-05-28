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
}
