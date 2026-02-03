package com.alefiengo.inventariovacunacionempleados.controller;

import com.alefiengo.inventariovacunacionempleados.domain.dto.ApiResponse;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoAdminDTO;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoGetAllDTO;
import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;
import com.alefiengo.inventariovacunacionempleados.domain.mapper.MapStructMapper;
import com.alefiengo.inventariovacunacionempleados.service.EmpleadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/admin/empleados")
@ConditionalOnProperty(prefix = "app", name = "controller.enable-dto", havingValue = "true")
@Tag(name = "Rol ADMINISTRADOR", description = "Acciones relacionadas con los empleados")
public class EmpleadoAdminController {

    private final EmpleadoService empleadoService;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public EmpleadoAdminController(EmpleadoService empleadoService, MapStructMapper mapStructMapper) {
        this.empleadoService = empleadoService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmpleadoAdminDTO>>> obtenerEmpleados() {
        List<Empleado> empleados = convertirALista(empleadoService.findAll());
        List<EmpleadoAdminDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoAdminDto)
                .collect(Collectors.toList());

        String mensaje = empleadosDTO.isEmpty() ? "No se encontraron empleados" : null;
        return ResponseEntity.ok(new ApiResponse<>(true, mensaje, empleadosDTO, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpleadoAdminDTO>> obtenerEmpleadoPorId(@PathVariable Long id) {
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (oEmpleado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, String.format("No se encontró al empleado con el ID: %d", id), null, null));
        }

        EmpleadoAdminDTO empleadoDTO = mapStructMapper.empleadoAdminDto(oEmpleado.get());
        return ResponseEntity.ok(new ApiResponse<>(true, null, empleadoDTO, null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmpleadoAdminDTO>> crearEmpleado(@Valid @RequestBody EmpleadoAdminDTO empleado, BindingResult resultado) {
        String cedula = empleado.getCedula();
        Empleado empleadoRegistrar;
        Optional<Empleado> oEmpleadoCedula = empleadoService.filtrarPorCedula(cedula);

        if (resultado.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", null, mapearErrores(resultado)));
        }

        if (oEmpleadoCedula.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, String.format("El empleado con cédula %s, ya se encuentra registrado", cedula), null, null));
        }

        empleadoRegistrar = mapStructMapper.empleadoAdmin(empleado);
        empleadoRegistrar.setUsuario(cedula);
        empleadoRegistrar.setContrasenia(cedula);

        Empleado guardado = empleadoService.save(empleadoRegistrar);
        EmpleadoAdminDTO respuesta = mapStructMapper.empleadoAdminDto(guardado);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Empleado creado", respuesta, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmpleadoAdminDTO>> actualizarEmpleado(@PathVariable Long id, @Valid @RequestBody EmpleadoAdminDTO empleado, BindingResult resultado) {
        EmpleadoAdminDTO empleadoActualizado;
        Optional<Empleado> oEmpleado = empleadoService.findById(id);
        Optional<Empleado> oEmpleadoCedula = empleadoService.filtrarPorCedula(empleado.getCedula());

        if (resultado.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", null, mapearErrores(resultado)));
        }

        if (oEmpleado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, String.format("No se encontró al empleado con el ID: %d", id), null, null));
        }

        if (oEmpleadoCedula.isPresent() && !oEmpleadoCedula.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, String.format("El empleado con cédula %s, ya se encuentra registrado", empleado.getCedula()), null, null));
        }

        empleadoActualizado = mapStructMapper.empleadoAdminDto(oEmpleado.get());
        empleadoActualizado.setCedula(empleado.getCedula());
        empleadoActualizado.setNombres(empleado.getNombres());
        empleadoActualizado.setApellidos(empleado.getApellidos());
        empleadoActualizado.setCorreoElectronico(empleado.getCorreoElectronico());

        Empleado guardado = empleadoService.save(mapStructMapper.empleadoAdmin(empleadoActualizado));
        EmpleadoAdminDTO respuesta = mapStructMapper.empleadoAdminDto(guardado);
        return ResponseEntity.ok(new ApiResponse<>(true, "Empleado actualizado", respuesta, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarEmpleado(@PathVariable Long id) {
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (oEmpleado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, String.format("No se encontraron empleados con el ID: %d", id), null, null));
        }

        empleadoService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Empleado eliminado", null, null));
    }

    @GetMapping("/por-estado-vacunacion/{estadoVacunacion}")
    public ResponseEntity<ApiResponse<List<EmpleadoGetAllDTO>>> obtenerEmpleadosPorEstadoVacunacion(@PathVariable String estadoVacunacion) {
        Optional<EstadoVacunacion> estado = EstadoVacunacion.fromCodigo(estadoVacunacion);
        if (estado.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "No existe el estado de vacunación ingresado", null, null));
        }

        List<Empleado> empleados = estado.get() == EstadoVacunacion.VACUNADO
                ? convertirALista(empleadoService.obtenerEmpleadosVacunados())
                : convertirALista(empleadoService.obtenerEmpleadosNoVacunados());

        List<EmpleadoGetAllDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .collect(Collectors.toList());

        String etiqueta = estado.get() == EstadoVacunacion.VACUNADO ? "Vacunados" : "No Vacunados";
        String mensaje = empleadosDTO.isEmpty() ? String.format("No se encontraron empleados %s", etiqueta) : null;

        return ResponseEntity.ok(new ApiResponse<>(true, mensaje, empleadosDTO, null));
    }

    @GetMapping("/por-tipo-vacuna/{tipoVacuna}")
    public ResponseEntity<ApiResponse<List<EmpleadoGetAllDTO>>> obtenerEmpleadosPorTipoVacuna(@PathVariable String tipoVacuna) {
        Optional<TipoVacuna> tipo = TipoVacuna.fromCodigo(tipoVacuna);
        if (tipo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "No existe el tipo de vacuna ingresado", null, null));
        }

        List<Empleado> empleados;
        switch (tipo.get()) {
            case SPUTNIK_V:
                empleados = convertirALista(empleadoService.obtenerEmpleadosSputnikV());
                break;
            case ASTRA_ZENECA:
                empleados = convertirALista(empleadoService.obtenerEmpleadosAstraZeneca());
                break;
            case PFIZER:
                empleados = convertirALista(empleadoService.obtenerEmpleadosPfizer());
                break;
            case JHONSON_AND_JHONSON:
                empleados = convertirALista(empleadoService.obtenerEmpleadosJhonsonAndJhonson());
                break;
            default:
                empleados = List.of();
        }

        List<EmpleadoGetAllDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .collect(Collectors.toList());

        String mensaje = empleadosDTO.isEmpty()
                ? String.format("No se encontraron empleados con la vacuna %s", nombreVacuna(tipo.get()))
                : null;

        return ResponseEntity.ok(new ApiResponse<>(true, mensaje, empleadosDTO, null));
    }

    @GetMapping("/por-rango-vacunacion/{fechaInicio}/{fechaFin}")
    public ResponseEntity<ApiResponse<List<EmpleadoGetAllDTO>>> obtenerEmpleadosPorRangoFechaVacunacion(
            @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaInicio,
            @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fechaFin
    ) {
        if (fechaFin.isBefore(fechaInicio)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "La fecha fin no puede ser anterior a la fecha inicio", null, null));
        }

        List<Empleado> empleados = convertirALista(empleadoService.filtrarPorRangoFechaVacunacion(fechaInicio, fechaFin));
        List<EmpleadoGetAllDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .collect(Collectors.toList());

        String mensaje = empleadosDTO.isEmpty()
                ? "No se encontraron empleados vacunados en el rango de fechas ingresadas"
                : null;

        return ResponseEntity.ok(new ApiResponse<>(true, mensaje, empleadosDTO, null));
    }

    private Map<String, String> mapearErrores(BindingResult resultado) {
        Map<String, String> errores = new HashMap<>();
        resultado.getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return errores;
    }

    private List<Empleado> convertirALista(Iterable<Empleado> empleados) {
        return StreamSupport.stream(empleados.spliterator(), false)
                .collect(Collectors.toList());
    }

    private String nombreVacuna(TipoVacuna tipoVacuna) {
        switch (tipoVacuna) {
            case SPUTNIK_V:
                return "Sputnik V";
            case ASTRA_ZENECA:
                return "AstraZeneca";
            case PFIZER:
                return "Pfizer";
            case JHONSON_AND_JHONSON:
                return "Jhonson & Jhonson";
            default:
                return tipoVacuna.name();
        }
    }
}
