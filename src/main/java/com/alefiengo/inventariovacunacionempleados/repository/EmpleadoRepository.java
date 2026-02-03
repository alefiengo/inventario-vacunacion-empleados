package com.alefiengo.inventariovacunacionempleados.repository;

import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {

    Optional<Empleado> findByCedula(String cedula);

    Iterable<Empleado> findByEstadoVacunacion(EstadoVacunacion estadoVacunacion);

    Iterable<Empleado> findByVacuna_TipoVacuna(TipoVacuna tipoVacuna);

    Iterable<Empleado> findByVacuna_FechaVacunacionBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
