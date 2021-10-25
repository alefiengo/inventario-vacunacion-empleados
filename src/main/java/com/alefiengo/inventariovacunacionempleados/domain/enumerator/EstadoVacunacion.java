package com.alefiengo.inventariovacunacionempleados.domain.enumerator;

public enum EstadoVacunacion {

    VACUNADO("V"), NO_VACUNADO("NV");

    private String codigo;

    private EstadoVacunacion(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
