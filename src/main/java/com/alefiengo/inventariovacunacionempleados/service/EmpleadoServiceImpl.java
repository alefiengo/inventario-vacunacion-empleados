package com.alefiengo.inventariovacunacionempleados.service;

import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
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
        return repository.filtrarPorCedula(cedula);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosVacunados() {
        return repository.obtenerEmpleadosVacunados();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosNoVacunados() {
        return repository.obtenerEmpleadosNoVacunados();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosSputnikV() {
        return repository.obtenerEmpleadosSputnikV();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosAstraZeneca() {
        return repository.obtenerEmpleadosAstraZeneca();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosPfizer() {
        return repository.obtenerEmpleadosPfizer();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> obtenerEmpleadosJhonsonAndJhonson() {
        return repository.obtenerEmpleadosJhonsonAndJhonson();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empleado> filtrarPorRangoFechaVacunacion(LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.filtrarPorRangoFechaVacunacion(fechaInicio, fechaFin);
    }
}