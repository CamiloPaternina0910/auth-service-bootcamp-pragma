package co.com.bancolombia.usecase.usuario;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.usuario.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;

    public Mono<Usuario> save(Usuario usuario) {
        return usuarioValidator.validarCreacionUsuario(usuario)
                .then(usuarioRepository.save(usuario));
    }

    public Flux<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Mono<Usuario> findById(String id) {
        return usuarioValidator.validarExistenciaUsuario(id);
    }

    public Mono<Usuario> update(Usuario usuario) {
        return usuarioValidator.validarEdicionUsuario(usuario)
                        .then(usuarioRepository.update(usuario));
    }

    public Mono<Void> delete(String id) {
        return usuarioValidator.validarExistenciaUsuario(id)
                .then(usuarioRepository.deleteById(id));
    }
}
