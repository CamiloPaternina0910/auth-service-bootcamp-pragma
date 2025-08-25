package co.com.bancolombia.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LecturaUsuarioDto {

    private String nombres;

    private String apellidos;

    private String correoElectronico;

    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    private BigDecimal salarioBase;
}
