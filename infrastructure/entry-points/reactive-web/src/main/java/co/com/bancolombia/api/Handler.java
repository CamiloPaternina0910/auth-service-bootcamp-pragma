package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CrearUsuarioDto;
import co.com.bancolombia.api.dto.EditarUsuarioDto;
import co.com.bancolombia.api.mapper.UsuarioMapper;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final ReactiveValidator reactiveValidator;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CrearUsuarioDto.class)
                .flatMap(reactiveValidator::validate)
                .map(usuarioMapper::toModel)
                .flatMap(usuarioUseCase::save)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EditarUsuarioDto.class)
                .flatMap(reactiveValidator::validate)
                .map(usuarioMapper::toModel)
                .flatMap(usuarioUseCase::update)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser));
    }

    public Mono<ServerResponse> listenFindAllUser(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuarioUseCase.findAll(), Usuario.class);
    }

    public Mono<ServerResponse> listenFindUserById(ServerRequest serverRequest) {
        String id =serverRequest.pathVariable("id");

        return usuarioUseCase.findById(id)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenFindUserByDocumentoIdentificacion(ServerRequest serverRequest) {
        String id =serverRequest.pathVariable("documentoIdentificacion");

        return usuarioUseCase.findByDocumentoIdentificacion(id)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenDeleteUserById(ServerRequest serverRequest){
        String id =serverRequest.pathVariable("id");

        return usuarioUseCase.delete(id)
                .then(ServerResponse.noContent().build());
    }
}
