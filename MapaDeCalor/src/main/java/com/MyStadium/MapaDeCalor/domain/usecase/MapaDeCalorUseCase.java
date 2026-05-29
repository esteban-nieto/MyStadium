package com.MyStadium.MapaDeCalor.domain.usecase;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.domain.entity.TicketType;
import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.domain.gateway.BoletoGateway;
import com.MyStadium.MapaDeCalor.domain.gateway.ZonaGateway;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class MapaDeCalorUseCase {
    private final ZonaGateway zonaGateway;
    private final BoletoGateway boletoGateway;

    public List<Zona> obtenerZonas(String conciertoId) {
        List<Zona> zonas = zonaGateway.buscarPorConciertoId(conciertoId);
        if (zonas.isEmpty()) {
            zonas = crearZonasPorDefecto(conciertoId);
        }
        return zonas;
    }

    private List<Zona> crearZonasPorDefecto(String conciertoId) {
        List<Zona> zonas = new ArrayList<>();
        int pattern = Math.abs(conciertoId.hashCode()) % 3;

        int[][] ocupaciones = pattern == 0 ? new int[][]{
            {0, 800},    // norte-alta
            {150, 600},  // norte-baja
            {400, 800},  // sur-alta
            {480, 600},  // sur-baja
            {350, 500},  // oriental-norte
            {340, 400},  // oriental-central
            {500, 500},  // oriental-sur
            {200, 500},  // occidental-norte
            {90, 400},   // occidental-cent
            {500, 500},  // occidental-sur
            {170, 200},  // palcos
            {50, 100}    // vip
        } : pattern == 1 ? new int[][]{
            {160, 800},  // norte-alta
            {450, 600},  // norte-baja
            {280, 800},  // sur-alta
            {0, 600},    // sur-baja
            {250, 500},  // oriental-norte
            {360, 400},  // oriental-central
            {0, 500},    // oriental-sur
            {500, 500},  // occidental-norte
            {340, 400},  // occidental-cent
            {400, 500},  // occidental-sur
            {200, 200},  // palcos
            {85, 100}    // vip
        } : new int[][]{
            {0, 800},    // norte-alta
            {0, 600},    // norte-baja
            {0, 800},    // sur-alta
            {0, 600},    // sur-baja
            {500, 500},  // oriental-norte
            {350, 400},  // oriental-central
            {300, 500},  // oriental-sur
            {350, 500},  // occidental-norte
            {400, 400},  // occidental-cent
            {300, 500},  // occidental-sur
            {180, 200},  // palcos
            {90, 100}    // vip
        };

        String[] nombres = {
            "norte-alta","norte-baja","sur-alta","sur-baja",
            "oriental-norte","oriental-central","oriental-sur",
            "occidental-norte","occidental-central","occidental-sur",
            "palcos","vip"
        };
        int[] precios = {35000,45000,35000,45000,65000,120000,65000,65000,120000,65000,150000,250000};

        for (int i = 0; i < 12; i++) {
            zonas.add(Zona.builder()
                .conciertoId(conciertoId)
                .nombre(nombres[i])
                .capacidad(ocupaciones[i][1])
                .ocupados(ocupaciones[i][0])
                .precio(precios[i])
                .build());
        }

        List<Zona> guardadas = new ArrayList<>();
        for (Zona z : zonas) {
            guardadas.add(zonaGateway.guardar(z));
        }
        return guardadas;
    }

    public Map<String, Object> comprarBoletos(String usuarioId, String conciertoId, String conciertoNombre,
                                              String artista, String zonaId, int cantidad,
                                              String estadio, LocalDateTime fechaEvento, String tipoEntrada) {
        Zona zona = zonaGateway.buscarPorId(zonaId)
                .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada"));

        if (zona.getOcupados() + cantidad > zona.getCapacidad())
            throw new IllegalArgumentException("No hay suficientes boletos disponibles en " + zona.getNombre());

        if (zona.getOcupados() >= zona.getCapacidad())
            throw new IllegalArgumentException("La zona " + zona.getNombre() + " está agotada");

        TicketType tt;
        try {
            tt = tipoEntrada != null ? TicketType.valueOf(tipoEntrada.toUpperCase()) : TicketType.GENERAL;
        } catch (IllegalArgumentException e) {
            tt = TicketType.GENERAL;
        }

        String orderCode = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        List<Boleto> boletos = new ArrayList<>();
        double total = zona.getPrecio() * cantidad;
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < cantidad; i++) {
            String codigo = "MST-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            String asiento = "A" + (zona.getOcupados() + i + 1);
            Boleto boleto = Boleto.builder()
                    .codigoUnico(codigo)
                    .usuarioId(usuarioId)
                    .conciertoId(conciertoId)
                    .conciertoNombre(conciertoNombre)
                    .artista(artista)
                    .zonaNombre(zona.getNombre())
                    .zonaPrecio(zona.getPrecio())
                    .asiento(asiento)
                    .totalPagado(zona.getPrecio())
                    .fechaCompra(now)
                    .estadio(estadio)
                    .fechaEvento(fechaEvento)
                    .codigoOrden(orderCode)
                    .tipoEntrada(tt)
                    .build();
            boletos.add(boletoGateway.guardar(boleto));
        }

        zonaGateway.actualizarOcupados(zonaId, zona.getOcupados() + cantidad);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("boletos", boletos);
        resultado.put("total", total);
        resultado.put("codigoOrden", orderCode);
        return resultado;
    }

    public List<Boleto> obtenerBoletosPorUsuario(String usuarioId) {
        return boletoGateway.buscarPorUsuarioId(usuarioId);
    }

    public Optional<Boleto> obtenerBoletoPorCodigo(String codigo) {
        return boletoGateway.buscarPorCodigo(codigo);
    }
}
