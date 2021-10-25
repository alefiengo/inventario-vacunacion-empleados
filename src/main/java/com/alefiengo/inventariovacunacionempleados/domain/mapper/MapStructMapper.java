package com.alefiengo.inventariovacunacionempleados.domain.mapper;

import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoAdminDTO;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoDTO;
import com.alefiengo.inventariovacunacionempleados.domain.dto.EmpleadoGetAllDTO;
import com.alefiengo.inventariovacunacionempleados.domain.dto.VacunaDTO;
import com.alefiengo.inventariovacunacionempleados.domain.entity.Empleado;
import com.alefiengo.inventariovacunacionempleados.domain.entity.Vacuna;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    EmpleadoAdminDTO empleadoAdminDto(Empleado empleado);

    Empleado empleadoAdmin(EmpleadoAdminDTO empleadoAdminDTO);

    EmpleadoDTO empleadoDto(Empleado empleado);

    Empleado empleado(EmpleadoDTO empleadoDTO);

    EmpleadoGetAllDTO empleadoGetAllDTO(Empleado empleado);

    VacunaDTO vacunaDTO(Vacuna vacuna);

    Vacuna vacuna(VacunaDTO vacunaDTO);
}
