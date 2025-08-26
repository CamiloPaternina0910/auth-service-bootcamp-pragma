package co.com.bancolombia.model.usuario.gateways;

import co.com.bancolombia.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UsuarioRepository {

    Mono<Usuario> save(Usuario usuario);

    Mono<Usuario> findById(String id);

    Mono<Usuario> findByCorreoElectronico(String correoElectronico);

    Flux<Usuario> findAll();

    Mono<Usuario> update(Usuario usuario);

    Mono<Void> deleteById(String id);

}
