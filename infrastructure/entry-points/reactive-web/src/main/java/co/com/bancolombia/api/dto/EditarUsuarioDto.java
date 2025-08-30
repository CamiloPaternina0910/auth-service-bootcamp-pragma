package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class EditarUsuarioDto {
    @NotBlank(message = "El id es requerido")
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombres del usuario", example = "Camilo")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellidos del usuario", example = "Paternina")
    private String apellidos;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    @Schema(description = "Correo electrónico", example = "camilo.paternina@example.com")
    private String correoElectronico;

    @NotBlank(message = "El documento de identificación es obligatorio")
    @Schema(description = "Documento de identificación del usuario", example = "10031232527")
    private String documentoIdentificacion;

    @Schema(description = "Fecha de nacimiento", example = "1995-08-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Dirección", example = "San Pelayo, Córdoba")
    private String direccion;

    @Schema(description = "Telefóno", example = "30012345")
    private String telefono;

    @NotNull(message = "El salario base es obligatorio")
    @Min(value = 0, message = "El salario base debe ser mayor a 0")
    @Max(value = 15000000, message = "El salario base no debe superar los 15,000,000")
    @Schema(description = "Salario base del usuario", example = "3500000")
    private BigDecimal salarioBase;
}
