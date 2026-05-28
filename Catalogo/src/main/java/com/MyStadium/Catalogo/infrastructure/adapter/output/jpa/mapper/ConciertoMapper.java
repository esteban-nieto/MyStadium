package com.MyStadium.Catalogo.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.infrastructure.adapter.output.jpa.ConciertoData;
import org.springframework.stereotype.Component;

@Component
public class ConciertoMapper {
    public Concierto toModel(ConciertoData data) {
        if (data == null) return null;
        return Concierto.builder().id(data.getId()).nombre(data.getNombre())
                .artista(data.getArtista()).estadio(data.getEstadio())
                .ciudad(data.getCiudad())
                .fecha(data.getFecha()).precioBase(data.getPrecioBase())
                .imagenUrl(data.getImagenUrl()).build();
    }
    public ConciertoData toData(Concierto model) {
        if (model == null) return null;
        return ConciertoData.builder().id(model.getId()).nombre(model.getNombre())
                .artista(model.getArtista()).estadio(model.getEstadio())
                .ciudad(model.getCiudad())
                .fecha(model.getFecha()).precioBase(model.getPrecioBase())
                .imagenUrl(model.getImagenUrl()).build();
    }
}
