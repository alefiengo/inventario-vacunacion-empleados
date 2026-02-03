package com.alefiengo.inventariovacunacionempleados.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.*;

@Getter
@Setter
public class EmpleadoAdminDTO {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe contener exactamente 10 dígitos")
    @Pattern(regexp = "[0-9]+", message = "El número de cédula debe contener números únicamente")
    @JsonProperty("cedula")
    private String cedula;

    @NotBlank(message = "Los nombres son obligatorios")
    @Pattern(regexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", message = "Los nombres no deben contener números o caracteres especiales")
    @JsonProperty("nombres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Pattern(regexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", message = "Los apellidos no deben contener números o caracteres especiales")
    @JsonProperty("apellidos")
    private String apellidos;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    @JsonProperty("correoElectronico")
    private String correoElectronico;
}
