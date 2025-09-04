package co.com.bancolombia.model.jwt.gateways;

import reactor.core.publisher.Mono;

public interface JwtService {
    public Mono<String> logear(String correoElectronico, String clave);

    public Mono<String> encriptarClave(String clave);
}
