package com.alefiengo.inventariovacunacionempleados.controller;

import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoAdminDTO;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/empleados")
@ConditionalOnProperty(prefix = "app", name = "controller.enable-dto", havingValue = "true")
@Api(value = "Acciones relacionadas con los empleados", tags = "Rol ADMINISTRADOR")
public class EmpleadoAdminController {

    private final EmpleadoService empleadoService;
    private final MapStructMapper mapStructMapper;

    @Autowired
    public EmpleadoAdminController(EmpleadoService empleadoService, MapStructMapper mapStructMapper) {
        this.empleadoService = empleadoService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping
    public ResponseEntity<?> obtenerEmpleados() {
        Map<String, Object> mensaje = new HashMap<>();
        List<Empleado> empleados = (List<Empleado>) empleadoService.findAll();

        if (empleados.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", "No se encontraron empleados");
            return ResponseEntity.badRequest().body(mensaje);
        }

        List<EmpleadoAdminDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoAdminDto)
                .collect(Collectors.toList());

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadosDTO);

        return ResponseEntity.ok(mensaje);
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

        Optional<EmpleadoAdminDTO> oEmpleadoDTO = oEmpleado
                .stream()
                .map(mapStructMapper::empleadoAdminDto)
                .findFirst();

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", oEmpleadoDTO.get());

        return ResponseEntity.ok(mensaje);
    }

    @PostMapping
    public ResponseEntity<?> crearEmpleado(@Valid @RequestBody EmpleadoAdminDTO empleado, BindingResult resultado) {
        Map<String, Object> mensaje = new HashMap<>();
        Map<String, Object> validaciones = new HashMap<>();
        String cedula = empleado.getCedula();
        Empleado empleadoRegistrar;
        Optional<Empleado> oEmpleadoCedula = empleadoService.filtrarPorCedula(cedula);

        if (resultado.hasErrors()) {
            resultado.getFieldErrors()
                    .forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));

            return ResponseEntity.badRequest().body(validaciones);
        }

        if (oEmpleadoCedula.isPresent()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", String.format("El empleado con cédula %s, ya se encuentra registrado", cedula));
            return ResponseEntity.badRequest().body(mensaje);
        }

        empleadoRegistrar = mapStructMapper.empleadoAdmin(empleado);
        empleadoRegistrar.setUsuario(cedula);
        empleadoRegistrar.setContrasenia(cedula);

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadoService.save(empleadoRegistrar));

        return ResponseEntity.ok(mensaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEmpleado(@PathVariable Long id, @Valid @RequestBody EmpleadoAdminDTO empleado, BindingResult resultado) {
        Map<String, Object> mensaje = new HashMap<>();
        Map<String, Object> validaciones = new HashMap<>();
        EmpleadoAdminDTO empleadoActualizado;
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

        empleadoActualizado = mapStructMapper.empleadoAdminDto(oEmpleado.get());
        empleadoActualizado.setCedula(empleado.getCedula());
        empleadoActualizado.setNombres(empleado.getNombres());
        empleadoActualizado.setApellidos(empleado.getApellidos());
        empleadoActualizado.setCorreoElectronico(empleado.getCorreoElectronico());

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadoService.save(mapStructMapper.empleadoAdmin(empleadoActualizado)));

        return ResponseEntity.ok(mensaje);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Long id) {
        Map<String, Object> mensaje = new HashMap<>();
        Optional<Empleado> oEmpleado = empleadoService.findById(id);

        if (oEmpleado.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", String.format("No se encontraron empleados con el ID: %d", id));
            return ResponseEntity.badRequest().body(mensaje);
        }

        empleadoService.deleteById(id);

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", null);

        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/por-estado-vacunacion/{estadoVacunacion}")
    public ResponseEntity<?> obtenerEmpleadosPorEstadoVacunacion(@PathVariable String estadoVacunacion) {
        Map<String, Object> mensaje = new HashMap<>();
        List<Empleado> empleados;
        String sEstadoVacunacion;

        if (estadoVacunacion.equals("V")) {
            sEstadoVacunacion = "Vacunados";
            empleados = (List<Empleado>) empleadoService.obtenerEmpleadosVacunados();
        } else if (estadoVacunacion.equals("NV")) {
            sEstadoVacunacion = "No Vacunados";
            empleados = (List<Empleado>) empleadoService.obtenerEmpleadosNoVacunados();
        } else {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", "No existe el estado de vacunación ingresado");
            return ResponseEntity.badRequest().body(mensaje);
        }

        if (empleados.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", String.format("No se encontraron empleados %s", sEstadoVacunacion));
            return ResponseEntity.badRequest().body(mensaje);
        }

        List<EmpleadoGetAllDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .collect(Collectors.toList());

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadosDTO);

        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/por-tipo-vacuna/{tipoVacuna}")
    public ResponseEntity<?> obtenerEmpleadosPorTipoVacuna(@PathVariable String tipoVacuna) {
        Map<String, Object> mensaje = new HashMap<>();
        List<Empleado> empleados;
        String sTipoVacuna;

        switch (tipoVacuna) {
            case "SV":
                sTipoVacuna = "Sputnik V";
                empleados = (List<Empleado>) empleadoService.obtenerEmpleadosSputnikV();
                break;
            case "AZ":
                sTipoVacuna = "AstraZeneca";
                empleados = (List<Empleado>) empleadoService.obtenerEmpleadosAstraZeneca();
                break;
            case "P":
                sTipoVacuna = "Pfizer";
                empleados = (List<Empleado>) empleadoService.obtenerEmpleadosPfizer();
                break;
            case "JJ":
                sTipoVacuna = "Jhonson & Jhonson";
                empleados = (List<Empleado>) empleadoService.obtenerEmpleadosJhonsonAndJhonson();
                break;
            default:
                mensaje.put("estado", Boolean.FALSE);
                mensaje.put("mensaje", "No existe el tipo de vacuna ingresado");
                return ResponseEntity.badRequest().body(mensaje);
        }

        if (empleados.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", String.format("No se encontraron empleados con la vacuna %s", sTipoVacuna));
            return ResponseEntity.badRequest().body(mensaje);
        }

        List<EmpleadoGetAllDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .collect(Collectors.toList());

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadosDTO);

        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/por-rango-vacunacion/{fechaInicio}/{fechaFin}")
    public ResponseEntity<?> obtenerEmpleadosPorRangoFechaVacunacion(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        Map<String, Object> mensaje = new HashMap<>();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fIni = LocalDate.parse(fechaInicio, format);
        LocalDate fFin = LocalDate.parse(fechaFin, format);

        List<Empleado> empleados = (List<Empleado>) empleadoService.filtrarPorRangoFechaVacunacion(fIni, fFin);

        if (empleados.isEmpty()) {
            mensaje.put("estado", Boolean.FALSE);
            mensaje.put("mensaje", "No se encontraron empleados vacunados en el rango de fechas ingresadas");
            return ResponseEntity.badRequest().body(mensaje);
        }

        List<EmpleadoGetAllDTO> empleadosDTO = empleados
                .stream()
                .map(mapStructMapper::empleadoGetAllDTO)
                .collect(Collectors.toList());

        mensaje.put("estado", Boolean.TRUE);
        mensaje.put("datos", empleadosDTO);

        return ResponseEntity.ok(mensaje);
    }
}
