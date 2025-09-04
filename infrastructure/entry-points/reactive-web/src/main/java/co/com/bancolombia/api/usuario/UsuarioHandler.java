package co.com.bancolombia.api.usuario;

import co.com.bancolombia.api.ReactiveValidator;
import co.com.bancolombia.api.usuario.dto.CrearUsuarioDto;
import co.com.bancolombia.api.usuario.dto.EditarUsuarioDto;
import co.com.bancolombia.api.mapper.UsuarioMapper;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.usecase.usuario.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioHandler {

    private final UsuarioUseCase usuarioUseCase;
    private final UsuarioMapper usuarioMapper;
    private final ReactiveValidator reactiveValidator;
    private final PasswordEncoder passwordEncoder;

    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CrearUsuarioDto.class)
                .flatMap(reactiveValidator::validate)
                .map(usuarioDto -> {
                    Usuario usuario = usuarioMapper.toModel(usuarioDto);
                    usuario.setClave(passwordEncoder.encode(usuario.getClave()));
                    return usuario;
                })
                .flatMap(usuarioUseCase::save)
                .map(usuarioMapper::toResponse)
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
