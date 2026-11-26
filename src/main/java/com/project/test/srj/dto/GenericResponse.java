package com.project.test.srj.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {

    private String codigo;
    private String mensaje;
    private Instant fechaHora;
}
