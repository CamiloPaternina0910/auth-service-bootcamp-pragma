package co.com.bancolombia.model.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

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
