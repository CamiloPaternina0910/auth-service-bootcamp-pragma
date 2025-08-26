package co.com.bancolombia.api;


import co.com.bancolombia.usecase.usuario.exception.DominioException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        HttpStatus status;
        String message = ex.getMessage();

        if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof DominioException dominioException) {
            status = HttpStatus.valueOf(dominioException.getEstado());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Unexpected error: " + message;
        }

        String body = String.format(
                "{ \"status\": %d, \"error\": \"%s\", \"message\": \"%s\" }",
                status.value(),
                status.getReasonPhrase(),
                message
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }
}