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

    public static java.util.Optional<EstadoVacunacion> fromCodigo(String codigo) {
        if (codigo == null) {
            return java.util.Optional.empty();
        }
        String normalizado = codigo.trim().toUpperCase();
        return java.util.Arrays.stream(values())
                .filter(valor -> valor.codigo.equalsIgnoreCase(normalizado) || valor.name().equalsIgnoreCase(normalizado))
                .findFirst();
    }
}
