package com.alefiengo.inventariovacunacionempleados.domain.dto;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmpleadoGetAllDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cedula")
    private String cedula;

    @JsonProperty("nombres")
    private String nombres;

    @JsonProperty("apellidos")
    private String apellidos;

    @JsonProperty("correoElectronico")
    private String correoElectronico;

    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;

    @JsonProperty("domicilio")
    private String domicilio;

    @JsonProperty("telefonoMovil")
    private String telefonoMovil;

    @JsonProperty("estadoVacunacion")
    private EstadoVacunacion estadoVacunacion;

    @JsonProperty("vacuna")
    VacunaDTO vacuna;
}
