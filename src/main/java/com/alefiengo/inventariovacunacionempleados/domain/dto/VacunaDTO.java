package com.alefiengo.inventariovacunacionempleados.domain.dto;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
public class VacunaDTO {

    @JsonProperty("id")
    private Long id;

    @NotNull(message = "El tipo de vacuna aplicado es obligatorio")
    @JsonProperty("tipoVacuna")
    private TipoVacuna tipoVacuna;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "La fecha de vacunación es obligatoria")
    @PastOrPresent(message = "La fecha de vacunación debe ser anterior o igual a la fecha actual")
    @JsonProperty("fechaVacunacion")
    private LocalDate fechaVacunacion;


    @NotNull(message = "El número de dosis aplicados es obligatorio")
    @Positive(message = "El número ingresado debe ser mayor a 0")
    @JsonProperty("numeroDosis")
    private Integer numeroDosis;
}
