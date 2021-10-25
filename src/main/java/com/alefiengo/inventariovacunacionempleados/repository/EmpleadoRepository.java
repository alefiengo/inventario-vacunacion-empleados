package com.alefiengo.inventariovacunacionempleados.repository;

import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {

    @Query("SELECT e FROM Empleado e WHERE e.cedula = ?1")
    Optional<Empleado> filtrarPorCedula(String cedula);

    @Query("SELECT e FROM Empleado e WHERE e.estadoVacunacion = com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion.VACUNADO")
    Iterable<Empleado> obtenerEmpleadosVacunados();

    @Query("SELECT e FROM Empleado e WHERE e.estadoVacunacion = com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion.NO_VACUNADO")
    Iterable<Empleado> obtenerEmpleadosNoVacunados();

    @Query("SELECT e FROM Empleado e JOIN e.vacuna v WHERE v.tipoVacuna = com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna.SPUTNIK_V")
    Iterable<Empleado> obtenerEmpleadosSputnikV();

    @Query("SELECT e FROM Empleado e JOIN e.vacuna v WHERE v.tipoVacuna = com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna.ASTRA_ZENECA")
    Iterable<Empleado> obtenerEmpleadosAstraZeneca();

    @Query("SELECT e FROM Empleado e JOIN e.vacuna v WHERE v.tipoVacuna = com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna.PFIZER")
    Iterable<Empleado> obtenerEmpleadosPfizer();

    @Query("SELECT e FROM Empleado e JOIN e.vacuna v WHERE v.tipoVacuna = com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna.JHONSON_AND_JHONSON")
    Iterable<Empleado> obtenerEmpleadosJhonsonAndJhonson();

    @Query("SELECT e FROM Empleado e JOIN e.vacuna v WHERE v.fechaVacunacion BETWEEN ?1 AND ?2")
    Iterable<Empleado> filtrarPorRangoFechaVacunacion(LocalDate fechaInicio, LocalDate fechaFin);
}