package co.com.bancolombia.r2dbc.usuario.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    private String id;

    private String nombres;

    private String apellidos;

    private String correoElectronico;

    private String documentoIdentificacion;

    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    private BigDecimal salarioBase;

    private String idRol;
}
