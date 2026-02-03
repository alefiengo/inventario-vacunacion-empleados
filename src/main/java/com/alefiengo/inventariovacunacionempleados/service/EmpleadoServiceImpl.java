package com.alefiengo.inventariovacunacionempleados.service;

import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;
import com.alefiengo.inventariovacunacionempleados.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl extends GenericServiceImpl<Empleado, EmpleadoRepository> implements EmpleadoService {

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> filtrarPorCedula(String cedula) {
        return repository.findByCedula(cedula);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosVacunados() {
        return repository.findByEstadoVacunacion(EstadoVacunacion.VACUNADO);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosNoVacunados() {
        return repository.findByEstadoVacunacion(EstadoVacunacion.NO_VACUNADO);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosSputnikV() {
        return repository.findByVacuna_TipoVacuna(TipoVacuna.SPUTNIK_V);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosAstraZeneca() {
        return repository.findByVacuna_TipoVacuna(TipoVacuna.ASTRA_ZENECA);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosPfizer() {
        return repository.findByVacuna_TipoVacuna(TipoVacuna.PFIZER);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosJhonsonAndJhonson() {
        return repository.findByVacuna_TipoVacuna(TipoVacuna.JHONSON_AND_JHONSON);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> filtrarPorRangoFechaVacunacion(LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.findByVacuna_FechaVacunacionBetween(fechaInicio, fechaFin);
    }
}
