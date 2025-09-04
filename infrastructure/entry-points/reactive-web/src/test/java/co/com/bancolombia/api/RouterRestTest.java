package co.com.bancolombia.api;

import co.com.bancolombia.api.usuario.UsuarioHandler;
import co.com.bancolombia.api.usuario.dto.CrearUsuarioDto;
import co.com.bancolombia.api.usuario.dto.EditarUsuarioDto;
import co.com.bancolombia.api.mapper.UsuarioMapper;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.usecase.usuario.UsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, UsuarioHandler.class})
@TestConstructor(autowireMode = AutowireMode.ALL)
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UsuarioUseCase usuarioUseCase;

    @MockitoBean
    private UsuarioMapper usuarioMapper;

    @MockitoBean
    private ReactiveValidator reactiveValidator;

    private Usuario buildUsuario() {
        return new Usuario(
                "1",
                "Camilo",
                "Paternina",
                "camilo@example.com",
                "1003717195",
                LocalDate.of(1995, 5, 20),
                "Calle 123",
                "3001234567",
                new BigDecimal("5000.00")
        );
    }

    @Test
    void testSaveUser() {
        CrearUsuarioDto dto = new CrearUsuarioDto();
        Usuario usuario = buildUsuario();

        when(reactiveValidator.validate(any(CrearUsuarioDto.class))).thenReturn(Mono.just(dto));
        when(usuarioMapper.toModel(dto)).thenReturn(usuario);
        when(usuarioUseCase.save(usuario)).thenReturn(Mono.just(usuario));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombres").isEqualTo("Camilo")
                .jsonPath("$.apellidos").isEqualTo("Paternina")
                .jsonPath("$.correoElectronico").isEqualTo("camilo@example.com")
                .jsonPath("$.salarioBase").isEqualTo(5000.00);
    }

    @Test
    void testUpdateUser() {
        EditarUsuarioDto dto = new EditarUsuarioDto();
        Usuario usuario = buildUsuario();
        usuario.setNombres("Camilo Mod");

        when(reactiveValidator.validate(any(EditarUsuarioDto.class))).thenReturn(Mono.just(dto));
        when(usuarioMapper.toModel(dto)).thenReturn(usuario);
        when(usuarioUseCase.update(usuario)).thenReturn(Mono.just(usuario));

        webTestClient.put()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Camilo Mod");
    }

    @Test
    void testFindAllUsers() {
        Usuario usuario = buildUsuario();

        when(usuarioUseCase.findAll()).thenReturn(Flux.just(usuario));

        webTestClient.get()
                .uri("/api/v1/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].correoElectronico").isEqualTo("camilo@example.com");


    }

    @Test
    void testFindUserById_found() {
        Usuario usuario = buildUsuario();

        when(usuarioUseCase.findById("1")).thenReturn(Mono.just(usuario));

        webTestClient.get()
                .uri("/api/v1/usuarios/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombres").isEqualTo("Camilo")
                .jsonPath("$.apellidos").isEqualTo("Paternina");
    }

    @Test
    void testFindUserById_notFound() {
        when(usuarioUseCase.findById("2")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/usuarios/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testFindUserByDocumentoIdentificacion_found() {
        Usuario usuario = buildUsuario();

        when(usuarioUseCase.findByDocumentoIdentificacion("1003717195")).thenReturn(Mono.just(usuario));

        webTestClient.get()
                .uri("/api/v1/usuarios/documentoIdentificacion/1003717195")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.nombres").isEqualTo("Camilo")
                .jsonPath("$.apellidos").isEqualTo("Paternina");
    }

    @Test
    void testFindUserByDocumentoIdentificacion_notFound() {
        when(usuarioUseCase.findByDocumentoIdentificacion("234534535")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/usuarios/documentoIdentificacion/234534535")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteUser() {
        when(usuarioUseCase.delete("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/usuarios/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
