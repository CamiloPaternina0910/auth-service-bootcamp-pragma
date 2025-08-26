package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CrearUsuarioDto;
import co.com.bancolombia.api.dto.EditarUsuarioDto;
import co.com.bancolombia.api.dto.LecturaUsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final Handler usuarioHandler;

    @Bean
    @RouterOperations({
            // Crear usuario
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            operationId = "saveUsuario",
                            summary = "Crear un usuario",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos del usuario a crear",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CrearUsuarioDto.class),
                                            examples = @ExampleObject(
                                                    name = "Ejemplo creación usuario",
                                                    value = "{\n" +
                                                            "  \"nombres\": \"Camilo\",\n" +
                                                            "  \"apellidos\": \"Paternina\",\n" +
                                                            "  \"correoElectronico\": \"camilo.paternina@example.com\",\n" +
                                                            "  \"fechaNacimiento\": \"1995-08-15\",\n" +
                                                            "  \"direccion\": \"Calle 123 #45-67\",\n" +
                                                            "  \"telefono\": \"3001234567\",\n" +
                                                            "  \"salarioBase\": 3500000\n" +
                                                            "}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Usuario creado exitosamente",
                                            content = @Content(schema = @Schema(implementation = LecturaUsuarioDto.class))
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }
                    )
            ),
            // Listar usuarios
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenFindAllUser",
                    operation = @Operation(
                            operationId = "findAllUsuarios",
                            summary = "Listar todos los usuarios",
                            tags = {"Usuarios"},
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Lista de usuarios",
                                    content = @Content(
                                            array = @ArraySchema(schema = @Schema(implementation = LecturaUsuarioDto.class))
                                    )
                            )
                    )
            ),
            // Buscar por ID
            @RouterOperation(
                    path = "/api/v1/usuarios/{id}",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenFindUserById",
                    operation = @Operation(
                            operationId = "findUsuarioById",
                            summary = "Buscar usuario por id",
                            tags = {"Usuarios"},
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, required = true, example = "64f123abc"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario encontrado",
                                            content = @Content(schema = @Schema(implementation = LecturaUsuarioDto.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            // Editar usuario
            @RouterOperation(
                    path = "/api/v1/usuarios/{id}",
                    method = RequestMethod.PUT,
                    beanClass = Handler.class,
                    beanMethod = "listenUpdateUser",
                    operation = @Operation(
                            operationId = "updateUsuario",
                            summary = "Actualizar usuario por id",
                            tags = {"Usuarios"},
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, required = true, example = "64f123abc"),
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos del usuario a editar",
                                    content = @Content(
                                            schema = @Schema(implementation = EditarUsuarioDto.class),
                                            examples = @ExampleObject(
                                                    name = "Ejemplo edición usuario",
                                                    value = "{\n" +
                                                            "  \"id\": \"64f123abc\",\n" +
                                                            "  \"nombres\": \"Carlos\",\n" +
                                                            "  \"apellidos\": \"Ramírez\",\n" +
                                                            "  \"correoElectronico\": \"carlos.ramirez@example.com\",\n" +
                                                            "  \"fechaNacimiento\": \"1990-05-20\",\n" +
                                                            "  \"direccion\": \"Carrera 10 #20-30\",\n" +
                                                            "  \"telefono\": \"3209876543\",\n" +
                                                            "  \"salarioBase\": 4200000\n" +
                                                            "}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario actualizado",
                                            content = @Content(schema = @Schema(implementation = LecturaUsuarioDto.class))
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            ),
            // Eliminar usuario
            @RouterOperation(
                    path = "/api/v1/usuarios/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = Handler.class,
                    beanMethod = "listenDeleteUserById",
                    operation = @Operation(
                            operationId = "deleteUsuario",
                            summary = "Eliminar usuario por id",
                            tags = {"Usuarios"},
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, required = true, example = "64f123abc"),
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> usuarioRoutes(Handler handler) {
        return route(POST("/api/v1/usuarios"), usuarioHandler::listenSaveUser)
                .andRoute(GET("/api/v1/usuarios"), usuarioHandler::listenFindAllUser)
                .andRoute(GET("/api/v1/usuarios/{id}"), usuarioHandler::listenFindUserById)
                .andRoute(PUT("/api/v1/usuarios/{id}"), usuarioHandler::listenUpdateUser)
                .andRoute(DELETE("/api/v1/usuarios/{id}"), usuarioHandler::listenDeleteUserById);
    }

}
