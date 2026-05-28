package com.MyStadium.Catalogo.infrastructure.adapter.input.web;

import com.MyStadium.Catalogo.domain.entity.Concierto;
import com.MyStadium.Catalogo.domain.usecase.ConciertoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/conciertos")
@CrossOrigin(origins = "*")
public class ConciertoController {
    private final ConciertoUseCase conciertoUseCase;
    private final String imagesPath;

    public ConciertoController(ConciertoUseCase conciertoUseCase,
                               @Value("${app.images.path:}") String imagesPath) {
        this.conciertoUseCase = conciertoUseCase;
        this.imagesPath = imagesPath;
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagenConcierto(@PathVariable String id) {
        try {
            Concierto concierto = conciertoUseCase.obtenerConciertoPorId(id);
            if (concierto == null) return ResponseEntity.notFound().build();

            String imagenUrl = concierto.getImagenUrl();
            byte[] imageBytes = null;

            if (imagenUrl != null && !imagenUrl.isBlank()) {
                String fileName = new File(imagenUrl).getName();
                File imgFile = new File(imagesPath, fileName);
                if (imgFile.exists()) {
                    imageBytes = Files.readAllBytes(imgFile.toPath());
                }
            }

            if (imageBytes == null) {
                String artist = concierto.getArtista();
                if (artist != null) {
                    File dir = new File(imagesPath);
                    File[] files = dir.listFiles((d, name) -> {
                        String lower = name.toLowerCase();
                        String a = artist.toLowerCase().replaceAll("\\s+", "");
                        return lower.contains(a);
                    });
                    if (files != null && files.length > 0) {
                        imageBytes = Files.readAllBytes(files[0].toPath());
                    }
                }
            }

            if (imageBytes != null) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crearConcierto(@RequestBody Concierto concierto) {
        try { return ResponseEntity.ok(conciertoUseCase.guardarConcierto(concierto)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concierto> obtenerConcierto(@PathVariable String id) {
        Concierto concierto = conciertoUseCase.obtenerConciertoPorId(id);
        if (concierto != null) return ResponseEntity.ok(concierto);
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Concierto>> obtenerTodos() {
        return ResponseEntity.ok(conciertoUseCase.obtenerTodosLosConciertos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConcierto(@PathVariable String id) {
        if (conciertoUseCase.eliminarConcierto(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}
