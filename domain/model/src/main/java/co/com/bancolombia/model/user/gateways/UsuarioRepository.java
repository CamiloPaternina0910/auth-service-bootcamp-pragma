package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> save(Usuario usuario);

    Mono<Usuario> findByCorreoElectronico(String correoElectronico);

    Flux<Usuario> findAll();

    Mono<Usuario> update(Usuario usuario);

    Mono<Void> deleteById (String id);

}
