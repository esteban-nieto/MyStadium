package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.ZonaData;
import org.springframework.stereotype.Component;

@Component
public class ZonaMapper {
    public Zona toModel(ZonaData data) {
        if (data == null) return null;
        return Zona.builder().id(data.getId()).conciertoId(data.getConciertoId())
                .nombre(data.getNombre()).capacidad(data.getCapacidad())
                .ocupados(data.getOcupados()).precio(data.getPrecio()).build();
    }
    public ZonaData toData(Zona model) {
        if (model == null) return null;
        return ZonaData.builder().id(model.getId()).conciertoId(model.getConciertoId())
                .nombre(model.getNombre()).capacidad(model.getCapacidad())
                .ocupados(model.getOcupados()).precio(model.getPrecio()).build();
    }
}
