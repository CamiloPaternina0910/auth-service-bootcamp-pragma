package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.r2dbc.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioReactiveRepositoryAdapterTest {

    @InjectMocks
    UsuarioReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UsuarioReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private UsuarioEntity usuarioEntity;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        usuarioEntity = new UsuarioEntity(
                "1",
                "Camilo",
                "Paternina",
                "camilo@example.com",
                "1003717195",
                null, "Calle 123",
                "3001234567",
                null
        );
        usuario = new Usuario(
                "1",
                "Camilo",
                "Paternina",
                "camilo@example.com",
                "1003717195",
                null, "Calle 123",
                "3001234567",
                null
        );
    }

    @Test
    void findById_whenUsuarioExists_shouldReturnUsuario() {
        when(repository.findById("1")).thenReturn(Mono.just(usuarioEntity));
        when(mapper.map(usuarioEntity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.findById("1");

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findById_whenUsuarioNotFound_shouldReturnEmpty() {
        when(repository.findById("2")).thenReturn(Mono.empty());

        Mono<Usuario> result = repositoryAdapter.findById("2");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findAll_shouldReturnUsuarios() {
        when(repository.findAll()).thenReturn(Flux.just(usuarioEntity));
        when(mapper.map(usuarioEntity, Usuario.class)).thenReturn(usuario);

        Flux<Usuario> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void saveUsuario_shouldReturnSavedUsuario() {
        when(repository.save(usuarioEntity)).thenReturn(Mono.just(usuarioEntity));
        when(mapper.map(usuario, UsuarioEntity.class)).thenReturn(usuarioEntity);
        when(mapper.map(usuarioEntity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.save(usuario);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void updateUsuario_shouldReturnUpdatedUsuario() {
        when(repository.save(usuarioEntity)).thenReturn(Mono.just(usuarioEntity));
        when(mapper.map(usuario, UsuarioEntity.class)).thenReturn(usuarioEntity);
        when(mapper.map(usuarioEntity, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.update(usuario);

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void deleteUsuarioById_shouldCompleteSuccessfully() {
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        Mono<Void> result = repositoryAdapter.deleteById("1");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findByCorreoElectronico_whenUsuarioExists_shouldReturnUsuario() {
        when(repository.findByCorreoElectronico("camilo@example.com"))
                .thenReturn(Mono.just(usuario));

        Mono<Usuario> result = repositoryAdapter.findByCorreoElectronico("camilo@example.com");

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findByCorreoElectronico_whenNotFound_shouldReturnEmpty() {
        when(repository.findByCorreoElectronico("notfound@example.com")).thenReturn(Mono.empty());

        Mono<Usuario> result = repositoryAdapter.findByCorreoElectronico("notfound@example.com");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findByDocumentoIdentificacion_whenUsuarioExists_shouldReturnUsuario() {
        when(repository.findByDocumentoIdentificacion("1003717195"))
                .thenReturn(Mono.just(usuario));

        Mono<Usuario> result = repositoryAdapter.findByDocumentoIdentificacion("1003717195");

        StepVerifier.create(result)
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findByDocumentoIdentificacion_whenNotFound_shouldReturnEmpty() {
        when(repository.findByDocumentoIdentificacion("33433444433")).thenReturn(Mono.empty());

        Mono<Usuario> result = repositoryAdapter.findByDocumentoIdentificacion("33433444433");

        StepVerifier.create(result)
                .verifyComplete();
    }
}
