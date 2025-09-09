package co.com.bancolombia.jwt;

import co.com.bancolombia.model.jwt.gateways.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PasswordEncryptorImp implements PasswordEncryptor {
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> encryptPassword(String plainPassword) {
        return Mono.just(passwordEncoder.encode(plainPassword));
    }

    @Override
    public Mono<Boolean> verifyPassword(String plainPassword, String encryptedPassword) {
        return Mono.just(passwordEncoder.matches(plainPassword, encryptedPassword));
    }
}
