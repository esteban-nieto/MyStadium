package com.MyStadium.Catalogo.domain.usecase;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.domain.gateway.ConciertoGateway;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class ConciertoUseCase {
    private final ConciertoGateway conciertoGateway;

    public Concierto guardarConcierto(Concierto concierto) {
        if (concierto.getNombre() == null || concierto.getArtista() == null)
            throw new IllegalArgumentException("Nombre y Artista son obligatorios");
        return conciertoGateway.guardar(concierto);
    }

    public Concierto obtenerConciertoPorId(String id) {
        return conciertoGateway.buscarPorId(id);
    }

    public List<Concierto> obtenerTodosLosConciertos() {
        return conciertoGateway.buscarTodos();
    }

    public boolean eliminarConcierto(String id) {
        try { conciertoGateway.eliminarPorId(id); return true; }
        catch (Exception e) { return false; }
    }
}
