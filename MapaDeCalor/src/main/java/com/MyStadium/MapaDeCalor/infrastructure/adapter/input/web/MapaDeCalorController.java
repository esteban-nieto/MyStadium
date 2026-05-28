package com.MyStadium.MapaDeCalor.infrastructure.adapter.input.web;

import com.MyStadium.MapaDeCalor.domain.entity.Boleto;
import com.MyStadium.MapaDeCalor.domain.entity.Zona;
import com.MyStadium.MapaDeCalor.domain.usecase.MapaDeCalorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mapa-calor")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MapaDeCalorController {
    private final MapaDeCalorUseCase mapaDeCalorUseCase;

    @GetMapping("/{conciertoId}/zonas")
    public ResponseEntity<List<Zona>> obtenerZonas(@PathVariable String conciertoId) {
        return ResponseEntity.ok(mapaDeCalorUseCase.obtenerZonas(conciertoId));
    }

    @PostMapping("/comprar")
    public ResponseEntity<?> comprarBoletos(@RequestBody Map<String, Object> request) {
        try {
            String usuarioId = (String) request.get("usuarioId");
            String conciertoId = (String) request.get("conciertoId");
            String conciertoNombre = (String) request.get("conciertoNombre");
            String artista = (String) request.get("artista");
            String zonaId = (String) request.get("zonaId");
            int cantidad = Integer.parseInt(request.get("cantidad").toString());
            String estadio = (String) request.get("estadio");
            String tipoEntrada = (String) request.get("tipoEntrada");
            LocalDateTime fechaEvento = request.get("fechaEvento") != null
                    ? LocalDateTime.parse((String) request.get("fechaEvento"))
                    : LocalDateTime.now();
            return ResponseEntity.ok(mapaDeCalorUseCase.comprarBoletos(
                    usuarioId, conciertoId, conciertoNombre, artista, zonaId, cantidad,
                    estadio, fechaEvento, tipoEntrada));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/boletos/{usuarioId}")
    public ResponseEntity<List<Boleto>> obtenerBoletos(@PathVariable String usuarioId) {
        return ResponseEntity.ok(mapaDeCalorUseCase.obtenerBoletosPorUsuario(usuarioId));
    }

    @GetMapping("/boleto/{codigo}")
    public ResponseEntity<?> obtenerBoleto(@PathVariable String codigo) {
        return mapaDeCalorUseCase.obtenerBoletoPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
