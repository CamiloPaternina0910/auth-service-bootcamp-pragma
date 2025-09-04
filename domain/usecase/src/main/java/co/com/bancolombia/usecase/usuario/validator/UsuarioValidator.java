package co.com.bancolombia.usecase.usuario.validator;

import co.com.bancolombia.model.jwt.gateways.JwtHelper;
import co.com.bancolombia.model.jwt.gateways.JwtService;
import co.com.bancolombia.model.rol.Rol;
import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.exception.*;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final JwtService jwtService;

    private final String NOMBRE_ROL_CLIENTE = "CLIENTE";

    public Mono<Usuario> validarCreacionUsuario(Usuario usuario) {
        return validarCorreoElectronicoCrearUsuario(usuario)
                .then(validarSalarioBase(usuario.getSalarioBase()))
                .then(existeRolPorNombre(NOMBRE_ROL_CLIENTE))
                .map(rol -> {
                    usuario.setIdRol(rol.getId());
                    return usuario;
                })
                .flatMap(usuarioClaveEncriptada -> jwtService.encriptarClave(usuarioClaveEncriptada.getClave())
                        .map(claveEncriptada -> {
                            usuarioClaveEncriptada.setClave(claveEncriptada);
                            return usuarioClaveEncriptada;
                        }));
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

    private Mono<Rol> existeRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .switchIfEmpty(Mono.error(new RolPorNombreNoEncontradoException(nombre)));
    }

    public Mono<Usuario> validarEdicionUsuario(Usuario usuario) {
        return existeUsuarioPorId(usuario.getId())
                .flatMap(existingUser ->
                        Mono.when(
                                validarCorreoElectronicoEditarUsuario(usuario),
                                validarSalarioBase(usuario.getSalarioBase())
                        ).then(Mono.just(existingUser))
                )
                .map(existingUser -> {
                    usuario.setIdRol(existingUser.getIdRol());
                    usuario.setClave(existingUser.getClave());
                    return usuario;
                });
    }

    public Mono<Usuario> existeUsuarioPorId(String id) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException(id)));
    }

    public Mono<Usuario> existeUsuarioPorDocumentoIdentificacion(String documentoIdentificacion) {
        return usuarioRepository.findByDocumentoIdentificacion(documentoIdentificacion)
                .switchIfEmpty(Mono.error(new UsuarioDocumentoIdentidadNoEncontrado(documentoIdentificacion)));
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
