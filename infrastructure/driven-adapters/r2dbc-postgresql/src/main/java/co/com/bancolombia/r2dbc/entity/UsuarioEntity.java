package co.com.bancolombia.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name="users")
@Entity
@Getter
@Setter
public class UsuarioEntity {

    private String nombres;

    private String apellidos;

    @Id
    @Column(unique = true)
    private String correoElectronico;

    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    private BigDecimal salarioBase;
}
