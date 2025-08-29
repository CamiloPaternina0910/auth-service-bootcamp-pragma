package co.com.bancolombia.config;

import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.usuario.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UsuarioUseCaseConfig {

    private final UsuarioRepository usuarioRepository;

    @Bean
    public UsuarioValidator usuarioValidator() {
        return new UsuarioValidator(usuarioRepository);
    }
}
