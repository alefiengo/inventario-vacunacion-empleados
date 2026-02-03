package com.alefiengo.inventariovacunacionempleados.domain.dto;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmpleadoUpdateDTO {

    @JsonFormat(pattern = "dd-MM-yyyy")
    @PastOrPresent(message = "La fecha de nacimiento debe ser anterior o igual a la fecha actual")
    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;

    @JsonProperty("domicilio")
    private String domicilio;

    @Pattern(regexp = "[0-9]+", message = "El número de teléfono móvil debe contener números únicamente")
    @JsonProperty("telefonoMovil")
    private String telefonoMovil;

    @NotNull(message = "El estado de vacunación es obligatorio")
    @JsonProperty("estadoVacunacion")
    private EstadoVacunacion estadoVacunacion;

    @JsonProperty("vacuna")
    @Valid
    private VacunaDTO vacuna;
}
