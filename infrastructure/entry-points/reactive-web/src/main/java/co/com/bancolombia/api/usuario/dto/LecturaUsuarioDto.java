package co.com.bancolombia.api.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LecturaUsuarioDto {
    private String id;

    @Schema(description = "Nombres del usuario", example = "Camilo")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Paternina")
    private String apellidos;

    @Schema(description = "Correo electrónico", example = "camilo.paternina@example.com")
    private String correoElectronico;

    @Schema(description = "Documento de identificación del usuario", example = "10031232527")
    private String documentoIdentificacion;

    @Schema(description = "Fecha de nacimiento", example = "1995-08-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Dirección", example = "San Pelayo, Córdoba")
    private String direccion;

    @Schema(description = "Telefóno", example = "30012345")
    private String telefono;


    @Schema(description = "Salario base del usuario", example = "3500000")
    private BigDecimal salarioBase;
}
