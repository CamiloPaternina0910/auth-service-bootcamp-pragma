package co.com.bancolombia.model.jwt.gateways;

import reactor.core.publisher.Mono;

public interface PasswordEncryptor {
    public Mono<String> encryptPassword(String plainPassword);

    public Mono<Boolean> verifyPassword(String plainPassword, String encryptedPassword);
}
