package co.com.bancolombia.model.rol.gateways;

import co.com.bancolombia.model.rol.Rol;
import reactor.core.publisher.Mono;

public interface RolRepository {

    Mono<Rol> findByNombre(String nombre);
    Mono<Rol> findById(String id);

}
