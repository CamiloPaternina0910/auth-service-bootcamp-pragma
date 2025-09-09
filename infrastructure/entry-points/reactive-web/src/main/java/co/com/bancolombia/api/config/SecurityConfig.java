package co.com.bancolombia.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerAuthenticationConverter converter;

    private final String PATH_USUARIO = "/api/v1/usuarios";
    private final String ROL_ADMIN = "ADMIN";
    private final String ROL_ASESOR = "ASESOR";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers(HttpMethod.POST, PATH_USUARIO).hasAnyAuthority(ROL_ADMIN, ROL_ASESOR)
                        .pathMatchers(HttpMethod.PUT, PATH_USUARIO).hasAnyAuthority(ROL_ADMIN, ROL_ASESOR)
                        .pathMatchers(HttpMethod.GET, PATH_USUARIO).hasAnyAuthority(ROL_ADMIN, ROL_ASESOR)
                        .pathMatchers(HttpMethod.GET, PATH_USUARIO + "/{id}").hasAnyAuthority(ROL_ADMIN, ROL_ASESOR)
                        .pathMatchers(HttpMethod.DELETE, PATH_USUARIO + "/{id}").hasAnyAuthority(ROL_ADMIN, ROL_ASESOR)
                        .anyExchange().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            byte[] bytes = "{\"message\" : \"Token invalido o ausente\" }".getBytes(StandardCharsets.UTF_8);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            return exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(bytes)));
                        })
                        .accessDeniedHandler((exchange, denied) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            byte[] bytes = "{\"message\" : \"No tienes permisos\" }".getBytes(StandardCharsets.UTF_8);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            return exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(bytes)));
                        })
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
