package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.Usuario;
import co.com.bancolombia.model.user.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.exception.CorreoElectronicoDuplicadoException;
import co.com.bancolombia.usecase.exception.UsuarioNoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> save(Usuario usuario) {
        return validateUserDoesNotExist(usuario.getCorreoElectronico())
                .then(usuarioRepository.save(usuario))
                .doOnNext(e -> log.info("Usuario ({}) registrado.", usuario.getCorreoElectronico()));
    }

    public Flux<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Mono<Usuario> findById(String id) {
        return validateUserExists(id);
    }

    public Mono<Usuario> update(Usuario usuario) {
        return validateUserExists(usuario.getCorreoElectronico())
                .flatMap(existing -> usuarioRepository.update(usuario))
                .doOnNext(e -> log.info("Usuario ({}) actualizado.", usuario.getCorreoElectronico()))
                .doOnError(e -> log.error("Usuario ({}) NO actualizado.", usuario.getCorreoElectronico()));
    }

    public Mono<Void> delete(String id) {
        return validateUserExists(id)
                .then(usuarioRepository.deleteById(id));
    }

    private Mono<Usuario> validateUserExists(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico)
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException(correoElectronico)))
                .doOnNext(e -> log.info("Usuario ({}) encontrado.", correoElectronico))
                .doOnError(e -> log.error("Usuario ({}) NO encontrado.", correoElectronico));
    }

    private Mono<Void> validateUserDoesNotExist(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico)
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        log.error("Usuario ({}) ya existe.", correoElectronico);
                        return Mono.error(new CorreoElectronicoDuplicadoException(correoElectronico));
                    }
                    log.info("Usuario ({}) no existe.");
                    return Mono.empty();
                });
    }
}
