package com.MyStadium.MapaDeCalor.domain.gateway;

import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import java.util.List;
import java.util.Optional;

public interface ZonaGateway {
    Zona guardar(Zona zona);
    Optional<Zona> buscarPorId(String id);
    List<Zona> buscarPorConciertoId(String conciertoId);
    void actualizarOcupados(String zonaId, int nuevosOcupados);
}
