package com.MyStadium.Auth.domain.entity;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    private String id;
    private String correo;
    private String contrasena;
    private String rol;
}
