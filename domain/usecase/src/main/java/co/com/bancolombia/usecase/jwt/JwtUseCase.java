package co.com.bancolombia.usecase.jwt;

import co.com.bancolombia.model.jwt.gateways.JwtService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtUseCase {

    private final JwtService jwtService;

    public Mono<String> logear(String correoElectronico, String clave){
        return jwtService.logear(correoElectronico, clave);
    }
}
