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

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmpleadoController.class)
@TestPropertySource(properties = "app.controller.enable-dto=true")
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @MockBean
    private MapStructMapper mapStructMapper;

    @Test
    void actualizarEmpleado_vacunadoSinVacuna_retorna400() throws Exception {
        when(empleadoService.findById(1L)).thenReturn(Optional.of(new Empleado()));

        String body = """
                {
                  "fechaNacimiento": "10-10-1990",
                  "domicilio": "Av. Principal 123",
                  "telefonoMovil": "0999999999",
                  "estadoVacunacion": "VACUNADO"
                }
                """;

        mockMvc.perform(put("/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Si el empleado está vacunado, la vacuna es obligatoria"));
    }

    @Test
    void actualizarEmpleado_noVacunadoConVacuna_retorna400() throws Exception {
        when(empleadoService.findById(1L)).thenReturn(Optional.of(new Empleado()));

        String body = """
                {
                  "fechaNacimiento": "10-10-1990",
                  "domicilio": "Av. Principal 123",
                  "telefonoMovil": "0999999999",
                  "estadoVacunacion": "NO_VACUNADO",
                  "vacuna": {
                    "tipoVacuna": "PFIZER",
                    "fechaVacunacion": "15-01-2022",
                    "numeroDosis": 2
                  }
                }
                """;

        mockMvc.perform(put("/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Si el empleado no está vacunado, no se debe enviar información de vacuna"));
    }
}
