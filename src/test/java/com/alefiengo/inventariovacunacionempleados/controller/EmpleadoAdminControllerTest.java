package com.alefiengo.inventariovacunacionempleados.controller;

import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.mapper.MapStructMapper;
import com.alefiengo.inventariovacunacionempleados.service.EmpleadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoAdminController.class)
@TestPropertySource(properties = "app.controller.enable-dto=true")
class EmpleadoAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Test
    void obtenerEmpleadoPorId_cuandoNoExiste_retorna404() throws Exception {
        when(empleadoService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/empleados/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(false))
                .andExpect(jsonPath("$.mensaje").value("No se encontró al empleado con el ID: 1"));
    }

    @Test
    void obtenerEmpleados_cuandoVacio_retornaListaVacia() throws Exception {
        when(empleadoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(true))
                .andExpect(jsonPath("$.datos").isArray())
                .andExpect(jsonPath("$.datos").isEmpty());
    }

    @Test
    void actualizarEmpleado_conCedulaDuplicada_retorna409() throws Exception {
        Empleado existente = new Empleado();
        existente.setId(1L);

        Empleado conMismaCedula = new Empleado();
        conMismaCedula.setId(2L);

        when(empleadoService.findById(1L)).thenReturn(Optional.of(existente));
        when(empleadoService.filtrarPorCedula("0123456789")).thenReturn(Optional.of(conMismaCedula));

        String body = """
                {
                  "cedula": "0123456789",
                  "nombres": "Ana",
                  "apellidos": "Perez",
                  "correoElectronico": "ana.perez@ejemplo.com"
                }
                """;

        mockMvc.perform(put("/admin/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.estado").value(false))
                .andExpect(jsonPath("$.mensaje").value("El empleado con cédula 0123456789, ya se encuentra registrado"));
    }
}
