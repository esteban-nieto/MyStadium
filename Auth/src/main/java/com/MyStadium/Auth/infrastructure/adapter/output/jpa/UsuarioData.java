package com.MyStadium.Auth.infrastructure.adapter.output.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "usuarios")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UsuarioData {
    @Id @UuidGenerator
    private String id;
    @Column(unique = true, nullable = false)
    private String correo;
    @Column(nullable = false)
    private String contrasena;
    private String rol;
}
