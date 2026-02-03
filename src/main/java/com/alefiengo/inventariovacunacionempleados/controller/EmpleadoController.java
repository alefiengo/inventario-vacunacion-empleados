package com.alefiengo.inventariovacunacionempleados.controller;

import com.alefiengo.inventariovacunacionempleados.domain.dto.ApiResponse;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoDTO;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoGetAllDTO;
import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import com.alefiengo.inventariovacunacionempleados.domain.mapper.MapStructMapper;
import com.alefiengo.inventariovacunacionempleados.service.EmpleadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
@ConditionalOnProperty(prefix = "app", name = "controller.enable-dto", havingValue = "true")
@Tag(name = "Rol EMPLEADO", description = "Acciones relacionadas con los empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService, MapStructMapper mapStructMapper) {
        this.empleadoService = empleadoService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpleadoGetAllDTO>> obtenerEmpleadoPorId(@PathVariable Long id) {
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (oEmpleado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, String.format("No se encontró al empleado con el ID: %d", id), null, null));
        }

        EmpleadoGetAllDTO empleadoDTO = mapStructMapper.empleadoGetAllDTO(oEmpleado.get());
        return ResponseEntity.ok(new ApiResponse<>(true, null, empleadoDTO, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpleadoDTO>> actualizarEmpleado(@PathVariable Long id,
                                                                       @Valid @RequestBody EmpleadoDTO empleado,
                                                                       BindingResult resultado
    ) {
        EmpleadoDTO empleadoActualizado;
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (resultado.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", null, mapearErrores(resultado)));
        }

        if (oEmpleado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, String.format("No se encontró al empleado con el ID: %d", id), null, null));
        }

        if (empleado.getEstadoVacunacion() != null) {
            if (empleado.getEstadoVacunacion() == EstadoVacunacion.VACUNADO && empleado.getVacuna() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Si el empleado está vacunado, la vacuna es obligatoria", null, null));
            }
            if (empleado.getEstadoVacunacion() == EstadoVacunacion.NO_VACUNADO && empleado.getVacuna() != null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Si el empleado no está vacunado, no se debe enviar información de vacuna", null, null));
            }
        }

        empleadoActualizado = mapStructMapper.empleadoDto(oEmpleado.get());
        empleadoActualizado.setFechaNacimiento(empleado.getFechaNacimiento());
        empleadoActualizado.setDomicilio(empleado.getDomicilio());
        empleadoActualizado.setTelefonoMovil(empleado.getTelefonoMovil());
        empleadoActualizado.setEstadoVacunacion(empleado.getEstadoVacunacion());
        empleadoActualizado.setVacuna(empleado.getVacuna());

        Empleado guardado = empleadoService.save(mapStructMapper.empleado(empleadoActualizado));
        EmpleadoDTO respuesta = mapStructMapper.empleadoDto(guardado);
        return ResponseEntity.ok(new ApiResponse<>(true, "Empleado actualizado", respuesta, null));
    }

    private Map<String, String> mapearErrores(BindingResult resultado) {
        Map<String, String> errores = new java.util.HashMap<>();
        resultado.getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return errores;
    }
}
