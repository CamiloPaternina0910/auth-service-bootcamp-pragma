package co.com.bancolombia.model.user;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {

    private String nombres;

    private String apellidos;

    private String correoElectronico;

    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    private BigDecimal salarioBase;
}
