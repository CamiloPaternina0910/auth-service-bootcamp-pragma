package co.com.bancolombia.api.security;

import co.com.bancolombia.model.jwt.gateways.JwtHelper;
import co.com.bancolombia.model.jwt.exception.TokenInvalidoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtHelper jwtHelper;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        Mono<String> usernameMono = jwtHelper.getUsernameDelToken(token);
        Mono<List<String>> rolesMono = jwtHelper.getRolesDelToken(token);

        return Mono.zip(usernameMono, rolesMono)
                .map(tuple ->
                        (Authentication) new UsernamePasswordAuthenticationToken(
                                tuple.getT1(),
                                null,
                                tuple.getT2().stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .toList()
                        )
                )
                .onErrorMap(Exception.class, e -> new TokenInvalidoException());
    }
}
