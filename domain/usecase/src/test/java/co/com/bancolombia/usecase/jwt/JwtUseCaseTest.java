package co.com.bancolombia.usecase.jwt;

import co.com.bancolombia.model.jwt.exception.CredencialesInvalidasException;
import co.com.bancolombia.model.jwt.gateways.JwtHelper;
import co.com.bancolombia.model.jwt.gateways.PasswordEncryptor;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @InjectMocks
    private JwtUseCase jwtUseCase;

    private Usuario usuarioValido;
    private final String CORREO_VALIDO = "usuario@test.com";
    private final String CLAVE_VALIDA = "clave123";
    private final String CLAVE_INVALIDA = "claveIncorrecta";
    private final String TOKEN_JWT = "token.jwt.generado";

    @BeforeEach
    void setUp() {
        usuarioValido = new Usuario();
        usuarioValido.setClave("claveEncriptada");
        usuarioValido.setCorreoElectronico(CORREO_VALIDO);
    }

    @Test
    void logear_UsuarioExistenteYClaveCorrecta_DeberiaGenerarToken() {
        when(usuarioRepository.findByCorreoElectronico(CORREO_VALIDO))
                .thenReturn(Mono.just(usuarioValido));
        when(passwordEncryptor.verifyPassword(CLAVE_VALIDA, usuarioValido.getClave()))
                .thenReturn(Mono.just(true));
        when(jwtHelper.generarToken(usuarioValido))
                .thenReturn(Mono.just(TOKEN_JWT));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .expectNext(TOKEN_JWT)
                .verifyComplete();
    }

    @Test
    void logear_UsuarioNoExistente_DeberiaLanzarCredencialesInvalidasException() {
        when(usuarioRepository.findByCorreoElectronico(CORREO_VALIDO))
                .thenReturn(Mono.empty());

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .expectError(CredencialesInvalidasException.class)
                .verify();
    }

    @Test
    void logear_ClaveIncorrecta_DeberiaLanzarCredencialesInvalidasException() {
        when(usuarioRepository.findByCorreoElectronico(CORREO_VALIDO))
                .thenReturn(Mono.just(usuarioValido));
        when(passwordEncryptor.verifyPassword(CLAVE_INVALIDA, usuarioValido.getClave()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_INVALIDA))
                .expectError(CredencialesInvalidasException.class)
                .verify();
    }

    @Test
    void logear_ErrorEnVerificacionPassword_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error en encriptación");

        when(usuarioRepository.findByCorreoElectronico(CORREO_VALIDO))
                .thenReturn(Mono.just(usuarioValido));
        when(passwordEncryptor.verifyPassword(CLAVE_VALIDA, usuarioValido.getClave()))
                .thenReturn(Mono.error(error));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void logear_ErrorEnGeneracionToken_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error generando token");

        when(usuarioRepository.findByCorreoElectronico(CORREO_VALIDO))
                .thenReturn(Mono.just(usuarioValido));
        when(passwordEncryptor.verifyPassword(CLAVE_VALIDA, usuarioValido.getClave()))
                .thenReturn(Mono.just(true));
        when(jwtHelper.generarToken(usuarioValido))
                .thenReturn(Mono.error(error));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .expectError(RuntimeException.class)
                .verify();
    }
}