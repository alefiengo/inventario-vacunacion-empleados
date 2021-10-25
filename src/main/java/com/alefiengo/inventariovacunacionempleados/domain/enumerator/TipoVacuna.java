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
}
