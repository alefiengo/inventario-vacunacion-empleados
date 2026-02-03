package com.alefiengo.inventariovacunacionempleados.domain.enumerator;

public enum TipoVacuna {

    SPUTNIK_V("SV"), ASTRA_ZENECA("AZ"), PFIZER("P"), JHONSON_AND_JHONSON("JJ");

    private String codigo;

    private TipoVacuna(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static java.util.Optional<TipoVacuna> fromCodigo(String codigo) {
        if (codigo == null) {
            return java.util.Optional.empty();
        }
        String normalizado = codigo.trim().toUpperCase();
        return java.util.Arrays.stream(values())
                .filter(valor -> valor.codigo.equalsIgnoreCase(normalizado) || valor.name().equalsIgnoreCase(normalizado))
                .findFirst();
    }
}
