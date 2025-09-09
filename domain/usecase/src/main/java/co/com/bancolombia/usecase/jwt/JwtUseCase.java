package co.com.bancolombia.usecase.jwt;

import co.com.bancolombia.model.jwt.exception.CredencialesInvalidasException;
import co.com.bancolombia.model.jwt.gateways.JwtHelper;
import co.com.bancolombia.model.jwt.gateways.PasswordEncryptor;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtUseCase {

    private final UsuarioRepository usuarioRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncryptor passwordEncryptor;

    public Mono<String> logear(String correoElectronico, String clave){
        return usuarioRepository.findByCorreoElectronico(correoElectronico)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CredencialesInvalidasException())))
                .flatMap(usuario ->
                        passwordEncryptor.verifyPassword(clave, usuario.getClave())
                                .flatMap(matches -> {
                                    if (!matches) {
                                        return Mono.error(new CredencialesInvalidasException());
                                    }
                                    return Mono.just(usuario);
                                })
                )
                .flatMap(jwtHelper::generarToken);
    }
}
