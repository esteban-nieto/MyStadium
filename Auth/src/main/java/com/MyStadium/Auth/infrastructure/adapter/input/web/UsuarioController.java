package com.MyStadium.Auth.infrastructure.adapter.input.web;

import com.MyStadium.Auth.domain.entity.Usuario;
import com.MyStadium.Auth.domain.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioUseCase usuarioUseCase;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try { return ResponseEntity.ok(usuarioUseCase.guardarUsuario(usuario)); }
        catch (DataIntegrityViolationException e) { return ResponseEntity.badRequest().body("El usuario ya existe"); }
        catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<?> iniciarSesion(@RequestBody java.util.Map<String, String> body) {
        try {
            String correo = body.get("correo");
            String contraseña = body.get("contraseña");
            return ResponseEntity.ok(usuarioUseCase.iniciarSesion(correo, contraseña));
        } catch (Exception e) { return ResponseEntity.badRequest().body(e.getMessage()); }
    }
}
