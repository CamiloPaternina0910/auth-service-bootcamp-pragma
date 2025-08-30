package co.com.bancolombia.model.usuario.gateways;

import co.com.bancolombia.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> save(Usuario usuario);

    Mono<Usuario> findById(String id);

    Mono<Usuario> findByCorreoElectronico(String correoElectronico);

    Mono<Usuario> findByDocumentoIdentificacion(String documentoIdentificacion);

    Flux<Usuario> findAll();

    Mono<Usuario> update(Usuario usuario);

    Mono<Void> deleteById(String id);

}
