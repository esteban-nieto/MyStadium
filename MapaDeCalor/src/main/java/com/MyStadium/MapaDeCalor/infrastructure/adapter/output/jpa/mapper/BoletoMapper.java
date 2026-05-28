package com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.mapper;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.domain.entity.TicketType;
import com.MyStadium.MapaDeCalor.infrastructure.adapter.output.jpa.BoletoData;
import org.springframework.stereotype.Component;

@Component
public class BoletoMapper {
    public Boleto toModel(BoletoData data) {
        if (data == null) return null;
        return Boleto.builder().id(data.getId()).codigoUnico(data.getCodigoUnico())
                .usuarioId(data.getUsuarioId()).conciertoId(data.getConciertoId())
                .conciertoNombre(data.getConciertoNombre()).artista(data.getArtista())
                .zonaNombre(data.getZonaNombre()).zonaPrecio(data.getZonaPrecio())
                .asiento(data.getAsiento()).totalPagado(data.getTotalPagado())
                .fechaCompra(data.getFechaCompra())
                .estadio(data.getEstadio())
                .fechaEvento(data.getFechaEvento())
                .codigoOrden(data.getCodigoOrden())
                .tipoEntrada(data.getTipoEntrada() != null ? TicketType.valueOf(data.getTipoEntrada()) : null)
                .build();
    }
    public BoletoData toData(Boleto model) {
        if (model == null) return null;
        return BoletoData.builder().id(model.getId()).codigoUnico(model.getCodigoUnico())
                .usuarioId(model.getUsuarioId()).conciertoId(model.getConciertoId())
                .conciertoNombre(model.getConciertoNombre()).artista(model.getArtista())
                .zonaNombre(model.getZonaNombre()).zonaPrecio(model.getZonaPrecio())
                .asiento(model.getAsiento()).totalPagado(model.getTotalPagado())
                .fechaCompra(model.getFechaCompra())
                .estadio(model.getEstadio())
                .fechaEvento(model.getFechaEvento())
                .codigoOrden(model.getCodigoOrden())
                .tipoEntrada(model.getTipoEntrada() != null ? model.getTipoEntrada().name() : null)
                .build();
    }
}
