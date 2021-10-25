package com.alefiengo.inventariovacunacionempleados.controller;

import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoDTO;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoGetAllDTO;
import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.mapper.MapStructMapper;
import com.alefiengo.inventariovacunacionempleados.service.EmpleadoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
@ConditionalOnProperty(prefix = "app", name = "controller.enable-dto", havingValue = "true")
@Api(value = "Acciones relacionadas con los empleados", tags = "Rol EMPLEADO")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService, MapStructMapper mapStructMapper) {
        this.empleadoService = empleadoService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEmpleadoPorId(@PathVariable Long id) {
        Map<String, Object> mensaje = new HashMap<>();
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (oEmpleado.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", String.format("No se encontró al empleado con el ID: %d", id));
            return ResponseEntity.badRequest().body(mensaje);
        }

        Optional<EmpleadoGetAllDTO> oEmpleadoGetAllDTO = oEmpleado
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .findFirst();

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", oEmpleadoGetAllDTO.get());

        return ResponseEntity.ok(mensaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Long id,
                                                @Valid @RequestBody EmpleadoDTO empleado,
                                                BindingResult resultado
    ) {
        Map<String, Object> mensaje = new HashMap<>();
        Map<String, Object> validaciones = new HashMap<>();
        EmpleadoDTO empleadoActualizado;
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (resultado.hasErrors()) {
            resultado.getFieldErrors()
                    .forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(validaciones);
        }

        if (oEmpleado.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", String.format("No se encontró al empleado con el ID: %d", id));
            return ResponseEntity.badRequest().body(mensaje);
        }

        empleadoActualizado = mapStructMapper.empleadoDto(oEmpleado.get());
        empleadoActualizado.setFechaNacimiento(empleado.getFechaNacimiento());
        empleadoActualizado.setDomicilio(empleado.getDomicilio());
        empleadoActualizado.setTelefonoMovil(empleado.getTelefonoMovil());
        empleadoActualizado.setEstadoVacunacion(empleado.getEstadoVacunacion());
        empleadoActualizado.setVacuna(empleado.getVacuna());

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadoService.save(mapStructMapper.empleado(empleadoActualizado)));

        return ResponseEntity.ok(mensaje);
    }
}
