package co.com.bancolombia.usecase.usuario;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.usuario.exception.CorreoElectronicoDuplicadoException;
import co.com.bancolombia.usecase.usuario.exception.SalarioInvalidoException;
import co.com.bancolombia.usecase.usuario.exception.UsuarioDocumentoIdentidadNoEncontrado;
import co.com.bancolombia.usecase.usuario.exception.UsuarioNoEncontradoException;
import co.com.bancolombia.usecase.usuario.validator.UsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    private UsuarioRepository repository;
    private UsuarioUseCase useCase;
    private Usuario usuario;

    @BeforeEach
     void setUp() {
        repository = Mockito.mock(UsuarioRepository.class);
        UsuarioValidator validator = new UsuarioValidator(repository);
        useCase = new UsuarioUseCase(repository, validator);

        usuario = new Usuario();
        usuario.setId("1");
        usuario.setNombres("Camilo");
        usuario.setApellidos("Paternina");
        usuario.setDocumentoIdentificacion("1003717195");
        usuario.setCorreoElectronico("camilo@example.com");
        usuario.setFechaNacimiento(LocalDate.of(1995, 8, 15));
        usuario.setSalarioBase(BigDecimal.valueOf(3500000));
    }

    @Test
    void saveUsuario_success() {
        when(repository.findByCorreoElectronico("camilo@example.com"))
                .thenReturn(Mono.empty());
        when(repository.save(any(Usuario.class)))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.save(usuario))
                .expectNext(usuario)
                .verifyComplete();

        verify(repository).save(usuario);
    }

    @Test
    void saveUsuario_correoDuplicado() {
        when(repository.findByCorreoElectronico("camilo@example.com"))
                .thenReturn(Mono.just(usuario));
        when(repository.save(usuario)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.save(usuario))
                .expectErrorMatches(throwable ->
                        throwable instanceof CorreoElectronicoDuplicadoException &&
                                throwable.getMessage().contains("camilo@example.com"))
                .verify();
    }

    @Test
    void saveUsuario_salarioMenorQueCero() {
        usuario.setSalarioBase(BigDecimal.valueOf(-1000));

        when(repository.findByCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.empty());
        when(repository.save(usuario)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.save(usuario))
                .expectError(SalarioInvalidoException.class)
                .verify();
    }

    @Test
    void saveUsuario_salarioMayorQueMaximo() {
        usuario.setSalarioBase(BigDecimal.valueOf(20_000_000));

        when(repository.findByCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.empty());
        when(repository.save(usuario)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.save(usuario))
                .expectError(SalarioInvalidoException.class)
                .verify();
    }

    @Test
    void findById_success() {
        when(repository.findById("1")).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.findById("1"))
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findById_notFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.findById("1"))
                .expectError(UsuarioNoEncontradoException.class)
                .verify();
    }

    @Test
    void findByDocumentoIdentificacion_success() {
        when(repository.findByDocumentoIdentificacion("1003717195")).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.findByDocumentoIdentificacion("1003717195"))
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void findByDocumentoIdentificacion_notFound() {
        when(repository.findByDocumentoIdentificacion("1003717195")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.findByDocumentoIdentificacion("1003717195"))
                .expectError(UsuarioDocumentoIdentidadNoEncontrado.class)
                .verify();
    }


    @Test
    void updateUsuario_success() {
        when(repository.findById("1")).thenReturn(Mono.just(usuario));
        when(repository.findByCorreoElectronico("camilo@example.com"))
                .thenReturn(Mono.empty());
        when(repository.update(usuario)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.update(usuario))
                .expectNext(usuario)
                .verifyComplete();
    }

    @Test
    void updateUsuario_correoDuplicado() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId("2"); // diferente ID
        otroUsuario.setCorreoElectronico("camilo@example.com");

        when(repository.findById("1")).thenReturn(Mono.just(usuario));
        when(repository.findByCorreoElectronico("camilo@example.com"))
                .thenReturn(Mono.just(otroUsuario));
        when(repository.update(usuario)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.update(usuario))
                .expectErrorMatches(throwable ->
                        throwable instanceof CorreoElectronicoDuplicadoException &&
                                throwable.getMessage().contains("camilo@example.com"))
                .verify();
    }

    @Test
    void updateUsuario_salarioInvalido() {
        usuario.setSalarioBase(BigDecimal.valueOf(20_000_000));

        when(repository.findById("1")).thenReturn(Mono.just(usuario));
        when(repository.findByCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.just(usuario));
        when(repository.update(usuario)).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.update(usuario))
                .expectError(SalarioInvalidoException.class)
                .verify();
    }

    @Test
    void deleteUsuario_success() {
        when(repository.findById("1")).thenReturn(Mono.just(usuario));
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.delete("1"))
                .verifyComplete();
    }

    @Test
    void findAllUsuarios() {
        when(repository.findAll()).thenReturn(Flux.just(usuario));

        StepVerifier.create(useCase.findAll())
                .expectNext(usuario)
                .verifyComplete();
    }
}
