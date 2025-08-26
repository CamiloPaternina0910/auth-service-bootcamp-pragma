package co.com.bancolombia.usecase.usuario;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.usuario.exception.CorreoElectronicoDuplicadoException;
import co.com.bancolombia.usecase.usuario.exception.UsuarioNoEncontradoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> save(Usuario usuario) {
        return validateCorreoElectronicoNotExist(usuario.getCorreoElectronico())
                .then(usuarioRepository.save(usuario));
    }

    public Flux<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Mono<Usuario> findById(String id) {
        return validateUserExists(id);
    }

    public Mono<Usuario> update(Usuario usuario) {
        return validateUserExists(usuario.getId())
                .flatMap(existingUser -> validateCorreoElectronicoNotExist(usuario.getCorreoElectronico())
                        .then(usuarioRepository.update(usuario)));
    }

    public Mono<Void> delete(String id) {
        return validateUserExists(id)
                .then(usuarioRepository.deleteById(id));
    }

    private Mono<Usuario> validateUserExists(String id) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException(id)));
    }

    private Mono<Void> validateCorreoElectronicoNotExist(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico)
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CorreoElectronicoDuplicadoException(correoElectronico));
                    }
                    return Mono.empty();
                });
    }
}
