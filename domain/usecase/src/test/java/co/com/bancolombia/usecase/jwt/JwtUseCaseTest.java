package co.com.bancolombia.usecase.jwt;

import co.com.bancolombia.model.jwt.exception.CredencialesInvalidasException;
import co.com.bancolombia.model.jwt.gateways.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUseCaseTest {
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtUseCase jwtUseCase;

    private final String CORREO_VALIDO = "usuario@test.com";
    private final String CLAVE_VALIDA = "clave123";
    private final String TOKEN_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

    @Test
    void testLogearExitoso() {
        when(jwtService.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .thenReturn(Mono.just(TOKEN_JWT));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .expectNext(TOKEN_JWT)
                .verifyComplete();

        verify(jwtService).logear(CORREO_VALIDO, CLAVE_VALIDA);
    }

    @Test
    void testLogearConCredencialesInvalidas() {
        when(jwtService.logear(CORREO_VALIDO, "clave-incorrecta"))
                .thenReturn(Mono.error(new CredencialesInvalidasException()));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, "clave-incorrecta"))
                .expectError(CredencialesInvalidasException.class)
                .verify();

        verify(jwtService).logear(CORREO_VALIDO, "clave-incorrecta");
    }

    @Test
    void testLogearConErrorInesperado() {
        when(jwtService.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .thenReturn(Mono.error(new RuntimeException("Error de conexión")));

        StepVerifier.create(jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA))
                .expectError(RuntimeException.class)
                .verify();

        verify(jwtService).logear(CORREO_VALIDO, CLAVE_VALIDA);
    }

    @Test
    void testLogearDelegaCorrectamenteAlService() {
        when(jwtService.logear(anyString(), anyString()))
                .thenReturn(Mono.just(TOKEN_JWT));

        jwtUseCase.logear(CORREO_VALIDO, CLAVE_VALIDA).block();

        verify(jwtService).logear(CORREO_VALIDO, CLAVE_VALIDA);
    }

    @Test
    void testLogearConParametrosNulos() {
        when(jwtService.logear(null, CLAVE_VALIDA))
                .thenReturn(Mono.error(new NullPointerException()));

        StepVerifier.create(jwtUseCase.logear(null, CLAVE_VALIDA))
                .expectError(NullPointerException.class)
                .verify();
    }
}
