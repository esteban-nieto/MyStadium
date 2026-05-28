package com.MyStadium.Notifications.infrastructure.adapter.input.web;

import com.MyStadium.Notifications.domain.entity.Notificacion;
import com.MyStadium.Notifications.domain.entity.ReciboCompra;
import com.MyStadium.Notifications.domain.usecase.NotificacionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificacionController {
    private final NotificacionUseCase notificacionUseCase;

    @PostMapping("/enviar-verificacion")
    public ResponseEntity<?> enviarVerificacion(@RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(notificacionUseCase.enviarEmailVerificacion(
                    request.get("email"), request.get("usuarioId")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enviar-recuperacion")
    public ResponseEntity<?> enviarRecuperacion(@RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(notificacionUseCase.enviarEmailRecuperacion(
                    request.get("email"), request.get("enlaceRecuperacion")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/recibo-compra")
    public ResponseEntity<?> enviarReciboCompra(@RequestBody ReciboCompra recibo) {
        try {
            return ResponseEntity.ok(notificacionUseCase.enviarReciboCompra(recibo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/enviar-recibo-compra")
    public ResponseEntity<?> enviarReciboCompraConPdf(@RequestBody Map<String, Object> request) {
        try {
            Object rawCodigos = request.get("codigos");
            List<String> codigos = new ArrayList<>();
            if (rawCodigos instanceof List) {
                for (Object o : (List<?>) rawCodigos) {
                    codigos.add(o != null ? o.toString() : null);
                }
            }
            String usuarioId = (String) request.get("usuarioId");
            String email = (String) request.get("email");
            if (codigos.isEmpty() || usuarioId == null || email == null)
                return ResponseEntity.badRequest().body(Map.of("error", "codigos, usuarioId y email son obligatorios"));
            return ResponseEntity.ok(notificacionUseCase.enviarReciboConPdf(codigos, usuarioId, email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/pdf/{codigo}")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable String codigo) {
        byte[] pdf = notificacionUseCase.obtenerPdf(codigo);
        if (pdf == null) return ResponseEntity.notFound().build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "recibo-" + codigo + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @PostMapping("/pdf-combinado")
    public ResponseEntity<byte[]> descargarPdfCombinado(@RequestBody Map<String, Object> request) {
        try {
            Object rawCodigos = request.get("codigos");
            List<String> codigos = new ArrayList<>();
            if (rawCodigos instanceof List) {
                for (Object o : (List<?>) rawCodigos) {
                    codigos.add(o != null ? o.toString() : null);
                }
            }
            byte[] pdf = notificacionUseCase.obtenerPdfMultiple(codigos);
            if (pdf == null) return ResponseEntity.notFound().build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "recibo-combinado.pdf");
            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerHistorial(@PathVariable String usuarioId) {
        return ResponseEntity.ok(notificacionUseCase.obtenerHistorialNotificaciones(usuarioId));
    }
}
