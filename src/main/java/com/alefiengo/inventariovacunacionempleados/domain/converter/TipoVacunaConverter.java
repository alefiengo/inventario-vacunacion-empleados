package com.alefiengo.inventariovacunacionempleados.domain.converter;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

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

        return Stream.of(TipoVacuna.values())
                .filter(c -> c.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}