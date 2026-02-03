package com.alefiengo.inventariovacunacionempleados.repository;

import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    void findByCedula_devuelveEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setCedula("0123456789");
        empleado.setNombres("Ana");
        empleado.setApellidos("Perez");
        empleado.setCorreoElectronico("ana@ejemplo.com");
        empleado.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        empleado.setDomicilio("Calle 1");
        empleado.setTelefonoMovil("0999999999");
        empleado.setEstadoVacunacion(EstadoVacunacion.NO_VACUNADO);

        empleadoRepository.save(empleado);

        Optional<Empleado> encontrado = empleadoRepository.findByCedula("0123456789");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCedula()).isEqualTo("0123456789");
    }
}
