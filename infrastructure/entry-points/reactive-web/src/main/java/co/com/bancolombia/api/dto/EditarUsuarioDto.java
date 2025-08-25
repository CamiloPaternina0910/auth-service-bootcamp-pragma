package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class EditarUsuarioDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    private String correoElectronico;

    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    @NotNull(message = "El salario base es obligatorio")
    @Min(value = 0, message = "El salario base debe ser mayor a 0")
    @Max(value = 15000000, message = "El salario base no debe superar los 15,000,000")
    private BigDecimal salarioBase;
}
