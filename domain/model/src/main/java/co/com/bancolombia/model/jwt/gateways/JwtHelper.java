package co.com.bancolombia.model.jwt.gateways;

import co.com.bancolombia.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JwtHelper {

    public Mono<String> generarToken(Usuario usuario);

    public Mono<List<String>> getRolesDelToken(String jwt);

    public Mono<String> getUsernameDelToken(String jwt);
}
