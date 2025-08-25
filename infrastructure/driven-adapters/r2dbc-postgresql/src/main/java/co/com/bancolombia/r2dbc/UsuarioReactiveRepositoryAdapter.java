package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.Usuario;
import co.com.bancolombia.model.user.gateways.UsuarioRepository;
import co.com.bancolombia.r2dbc.entity.UsuarioEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        String,
        UsuarioReactiveRepository
        > implements UsuarioRepository {
    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    @Transactional
    public Mono<Usuario> save(Usuario usuario) {
        return super.save(usuario);
    }

    @Override
    @Transactional
    public Mono<Usuario> update(Usuario usuario) {
        return super.findByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(existingUser -> super.save(usuario));
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        return super.findByCorreoElectronico(id)
                .flatMap(existingUser -> super.repository.deleteById(id));
    }
}
