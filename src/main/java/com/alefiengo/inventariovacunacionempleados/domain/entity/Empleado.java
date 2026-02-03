package com.alefiengo.inventariovacunacionempleados.domain.entity;

import com.alefiengo.inventariovacunacionempleados.domain.enumerator.EstadoVacunacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "empleado")
public class Empleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Basic
    @Column(name = "cedula", nullable = false, unique = true)
    private String cedula;

    @NonNull
    @Basic
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NonNull
    @Basic
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @NonNull
    @Basic
    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Basic
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Basic
    @Column(name = "domicilio")
    private String domicilio;

    @Basic
    @Column(name = "telefono_movil")
    private String telefonoMovil;

    @Basic
    @Column(name = "estado_vacunacion")
    private EstadoVacunacion estadoVacunacion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vacuna_id", referencedColumnName = "id")
    private Vacuna vacuna;

    @Basic
    @Column(name = "usuario")
    private String usuario;

    @Basic
    @Column(name = "contrasenia")
    private String contrasenia;

    @Column(name = "fecha_registro")
    @Basic
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_modificacion")
    @Basic
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
