package co.com.bancolombia.r2dbc.rol;

import co.com.bancolombia.model.rol.Rol;
import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.rol.entity.RolEntity;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        String,
        RolReactiveRepository
        > implements RolRepository {
    public RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Rol.class));
    }

    @Override
    public Mono<Rol> findByNombre(String nombre) {
        log.info("Buscando rol por nombre: {}", nombre);
        return this.repository.findByNombre(nombre)
                .doOnNext(rol -> log.info("Rol encontrado: {}", rol.getId()))
                .doOnError(e -> log.error("No se pudo encontrar el error por: {}", e.getMessage()));
    }

    @Override
    public Mono<Rol> findById(String id) {
        log.info("Buscando rol por id: {}", id);
        return super.findById(id)
                .doOnNext(rol -> log.info("Rol encontrado: {}", rol.getId()))
                .doOnError(e -> log.error("No se pudo encontrar el error por: {}", e.getMessage()));
    }
}
