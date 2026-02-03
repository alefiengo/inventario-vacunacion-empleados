package com.alefiengo.inventariovacunacionempleados.domain.converter;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoVacunaConverter implements AttributeConverter<TipoVacuna, String> {

    @Override
    public String convertToDatabaseColumn(TipoVacuna tipoVacuna) {
        if (tipoVacuna == null) {
            return null;
        }
        return tipoVacuna.getCodigo();
    }

    @Override
    public TipoVacuna convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }

        return TipoVacuna.fromCodigo(codigo)
                .orElseThrow(IllegalArgumentException::new);
    }
}
