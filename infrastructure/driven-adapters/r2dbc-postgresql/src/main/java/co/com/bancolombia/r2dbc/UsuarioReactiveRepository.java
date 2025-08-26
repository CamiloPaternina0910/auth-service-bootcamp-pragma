package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.r2dbc.entity.UsuarioEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UsuarioReactiveRepository extends ReactiveCrudRepository<UsuarioEntity, String>, ReactiveQueryByExampleExecutor<UsuarioEntity> {
    Mono<Usuario> findByCorreoElectronico(String correoElectronico);
}
