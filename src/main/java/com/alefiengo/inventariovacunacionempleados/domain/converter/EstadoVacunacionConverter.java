package com.alefiengo.inventariovacunacionempleados.domain.converter;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoVacunacionConverter implements AttributeConverter<EstadoVacunacion, String> {

    @Override
    public String convertToDatabaseColumn(EstadoVacunacion estadoVacunacion) {
        if (estadoVacunacion == null) {
            return null;
        }
        return estadoVacunacion.getCodigo();
    }

    @Override
    public EstadoVacunacion convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }

        return EstadoVacunacion.fromCodigo(codigo)
                .orElseThrow(IllegalArgumentException::new);
    }
}
