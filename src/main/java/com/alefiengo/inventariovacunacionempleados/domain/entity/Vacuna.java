package com.alefiengo.inventariovacunacionempleados.domain.entity;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.TipoVacuna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vacuna")
public class Vacuna implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Basic
    @Column(name = "tipo_vacuna", nullable = false)
    private TipoVacuna tipoVacuna;

    @NonNull
    @Basic
    @Column(name = "fecha_vacunacion", nullable = false)
    private LocalDate fechaVacunacion;

    @NonNull
    @Positive
    @Basic
    @Column(name = "numero_dosis", nullable = false)
    private Integer numeroDosis;

    @OneToOne(mappedBy = "vacuna")
    private Empleado empleado;

    @Basic
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Basic
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void beforePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
