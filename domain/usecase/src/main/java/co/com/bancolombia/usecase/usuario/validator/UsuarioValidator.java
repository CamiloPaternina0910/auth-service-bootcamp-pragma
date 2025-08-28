package co.com.bancolombia.usecase.usuario.validator;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.usuario.exception.CorreoElectronicoDuplicadoException;
import co.com.bancolombia.usecase.usuario.exception.SalarioInvalidoException;
import co.com.bancolombia.usecase.usuario.exception.UsuarioNoEncontradoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> validarCreacionUsuario(Usuario usuario){
        return validarCorreoElectronicoCrearUsuario(usuario)
                .then(validarSalarioBase(usuario.getSalarioBase()))
                .thenReturn(usuario);
    }

    private Mono<Void> validarCorreoElectronicoCrearUsuario(Usuario usuario) {
        return usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(existingUser -> Mono.error(
                        new CorreoElectronicoDuplicadoException(usuario.getCorreoElectronico())))
                .switchIfEmpty(Mono.empty())
                .then();
    }

    private Mono<Void> validarSalarioBase(BigDecimal salarioBase) {
        if (salarioBase == null || salarioBase.compareTo(BigDecimal.ZERO) < 0 || salarioBase.compareTo(BigDecimal.valueOf(15_000_000)) > 0) {
            return Mono.error(new SalarioInvalidoException());
        }
        return Mono.empty();
    }

    public Mono<Usuario> validarEdicionUsuario(Usuario usuario){
        return validarExistenciaUsuario(usuario.getId())
                .flatMap(existingUser -> validarCorreoElectronicoEditarUsuario(usuario)
                        .then(validarSalarioBase(usuario.getSalarioBase()))
                        .thenReturn(usuario)
                );
    }

    public Mono<Usuario> validarExistenciaUsuario(String id) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException(id)));
    }


    private Mono<Void> validarCorreoElectronicoEditarUsuario(Usuario usuario) {
        return usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(existingUser -> {
                    if (!existingUser.getId().equals(usuario.getId())) {
                        return Mono.error(new CorreoElectronicoDuplicadoException(usuario.getCorreoElectronico()));
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.empty())
                .then();
    }

}
