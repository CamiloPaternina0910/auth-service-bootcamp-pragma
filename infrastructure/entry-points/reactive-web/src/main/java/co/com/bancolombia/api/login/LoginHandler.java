package co.com.bancolombia.api.login;

import co.com.bancolombia.api.ReactiveValidator;
import co.com.bancolombia.api.login.dto.LoginDto;
import co.com.bancolombia.usecase.jwt.JwtUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoginHandler {

    private final JwtUseCase jwtUseCase;
    private final ReactiveValidator reactiveValidator;

    public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDto.class)
                .flatMap(reactiveValidator::validate)
                .flatMap(loginDto ->
                        jwtUseCase.logear(loginDto.getCorreoElectronico(), loginDto.getClave())
                                .flatMap(token -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(token)
                        )
                );
    }
}
