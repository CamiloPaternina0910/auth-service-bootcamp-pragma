package co.com.bancolombia.api;

import co.com.bancolombia.api.config.UsuarioPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final Handler usuarioHandler;
    private final UsuarioPath usuarioPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET(usuarioPath.getUsers()), usuarioHandler::listenFindAllUser)
                        .andRoute(POST(usuarioPath.getUsers()), usuarioHandler::listenSaveUser)
                        .andRoute(PUT(usuarioPath.getUsers()), usuarioHandler::listenUpdateUser)
                        .andRoute(GET(usuarioPath.getUserById()), usuarioHandler::listenFindUserById)
                        .and(route(DELETE(usuarioPath.getUserById()), usuarioHandler::listenDeleteUserById)
        );
    }
}
