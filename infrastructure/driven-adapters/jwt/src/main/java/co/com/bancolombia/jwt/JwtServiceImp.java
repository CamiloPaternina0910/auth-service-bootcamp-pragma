package co.com.bancolombia.jwt;

import co.com.bancolombia.model.jwt.exception.CredencialesInvalidasException;
import co.com.bancolombia.model.jwt.gateways.JwtHelper;
import co.com.bancolombia.model.jwt.gateways.JwtService;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImp implements JwtService {

    private final UsuarioRepository usuarioRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> logear(String correoElectronico, String clave) {
        log.info("Iniciando proceso de login para usuario: {}", correoElectronico);

        return usuarioRepository.findByCorreoElectronico(correoElectronico)
                .doOnNext(usuario -> log.debug("Usuario encontrado en BD: {}", usuario.getCorreoElectronico()))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario no encontrado: {}", correoElectronico);
                    return Mono.error(new CredencialesInvalidasException());
                }))
                .filter(usuario -> {
                    boolean matches = passwordEncoder.matches(clave, usuario.getClave());
                    if (!matches) {
                        log.warn("Contraseña incorrecta para usuario: {}", correoElectronico);
                    } else {
                        log.debug("Contraseña validada correctamente para usuario: {}", correoElectronico);
                    }
                    return matches;
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Credenciales inválidas para usuario: {}", correoElectronico);
                    return Mono.error(new CredencialesInvalidasException());
                }))
                .flatMap(usuario -> {
                    log.info("Generando token JWT para usuario: {}", correoElectronico);
                    return jwtHelper.generarToken(usuario);
                })
                .doOnSuccess(token -> log.info("Login exitoso para usuario: {}", correoElectronico))
                .doOnError(error -> {
                    if (error instanceof CredencialesInvalidasException) {
                        log.error("Error de autenticación para usuario: {} - {}", correoElectronico, error.getMessage());
                    } else {
                        log.error("Error inesperado durante login para usuario: {} - {}",
                                correoElectronico, error.getMessage(), error);
                    }
                });
    }

    @Override
    public Mono<String> encriptarClave(String clave) {
        log.debug("Encriptando contraseña");

        return Mono.just(passwordEncoder.encode(clave))
                .doOnNext(encodedPassword -> log.debug("Contraseña encriptada exitosamente"))
                .doOnError(error -> log.error("Error al encriptar contraseña: {}", error.getMessage(), error));
    }
}
