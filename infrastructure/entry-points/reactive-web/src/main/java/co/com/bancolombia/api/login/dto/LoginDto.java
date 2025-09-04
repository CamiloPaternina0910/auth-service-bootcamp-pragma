package co.com.bancolombia.api.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "El correo electronico es requerido")
    @Email(message = "Debe ser un correo electronico valido")
    @Schema(description = "Correo electronico del usuario", example = "paterninapcamio@gmail.com")
    private String correoElectronico;

    @NotEmpty(message = "La clave del usuario es requerida")
    @Schema(description = "Clave de acceso al sistema")
    private String clave;
}
