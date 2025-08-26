package co.com.bancolombia.usecase.usuario;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.usuario.exception.CorreoElectronicoDuplicadoException;
import co.com.bancolombia.usecase.usuario.exception.UsuarioNoEncontradoException;
import co.com.bancolombia.usecase.usuario.exception.SalarioInvalidoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> save(Usuario usuario) {
        return validateCorreoElectronicoForSave(usuario)
                .then(validateSalarioBase(usuario.getSalarioBase()))
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
                .flatMap(existingUser -> validateCorreoElectronicoForUpdate(usuario)
                        .then(validateSalarioBase(usuario.getSalarioBase()))
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

    private Mono<Void> validateCorreoElectronicoForSave(Usuario usuario) {
        return usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(existingUser -> Mono.error(
                        new CorreoElectronicoDuplicadoException(usuario.getCorreoElectronico())))
                .switchIfEmpty(Mono.empty())
                .then();
    }

    private Mono<Void> validateCorreoElectronicoForUpdate(Usuario usuario) {
        return usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(existingUser -> {
                    if (!existingUser.getId().equals(usuario.getId())) {
                        return Mono.error(new CorreoElectronicoDuplicadoException(usuario.getCorreoElectronico()));
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.empty())
                .then();
    }


    private Mono<Void> validateSalarioBase(BigDecimal salarioBase) {
        if (salarioBase == null || salarioBase.compareTo(BigDecimal.ZERO) < 0 || salarioBase.compareTo(BigDecimal.valueOf(15_000_000)) > 0) {
            return Mono.error(new SalarioInvalidoException());
        }
        return Mono.empty();
    }
}
