package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.r2dbc.entity.UsuarioEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@Repository
@Slf4j
public class UsuarioReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,
        UsuarioEntity,
        String,
        UsuarioReactiveRepository
        > implements UsuarioRepository {

    public UsuarioReactiveRepositoryAdapter(UsuarioReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
    }

    @Override
    @Transactional
    public Mono<Usuario> save(Usuario usuario) {
        return super.save(usuario)
                .doOnNext(e -> log.info("Usuario ({}) registrado.", usuario.getId()))
                .doOnError(e -> log.error(e.getMessage()));
    }

    @Override
    public Mono<Usuario> findByCorreoElectronico(String correoElectronico) {
        return this.repository.findByCorreoElectronico(correoElectronico);
    }

    @Override
    public Mono<Usuario> findByDocumentoIdentificacion(String documentoIdentificacion) {
        return this.repository.findByDocumentoIdentificacion(documentoIdentificacion);
    }

    @Override
    @Transactional
    public Mono<Usuario> update(Usuario usuario) {
        return super.save(usuario)
                .doOnNext(e -> log.info("Usuario ({}) actualizado.", usuario.getId()))
                .doOnError(e -> log.error(e.getMessage()));

    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        return super.repository.deleteById(id)
                .doOnSuccess(e -> log.info("Usuario ({}) eliminado.", id))
                .doOnError(e -> log.error(e.getMessage()));
    }
}
