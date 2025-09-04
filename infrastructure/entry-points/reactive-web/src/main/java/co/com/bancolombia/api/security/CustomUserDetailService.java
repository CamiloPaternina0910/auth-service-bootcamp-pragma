package co.com.bancolombia.api.security;

import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements ReactiveUserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return usuarioRepository.findByDocumentoIdentificacion(username)
                .flatMap(usuario -> rolRepository.findById(usuario.getIdRol())
                        .map(rol -> {
                            List<SimpleGrantedAuthority> simpleGrantedAuthority = new ArrayList<>();
                            SimpleGrantedAuthority simpleGrantedAuthority1 = new SimpleGrantedAuthority(rol.getNombre());
                            simpleGrantedAuthority.add(simpleGrantedAuthority1);

                            return User.builder()
                                    .username(usuario.getCorreoElectronico())
                                    .authorities(simpleGrantedAuthority)
                                    .password(usuario.getClave())
                                    .disabled(false)
                                    .accountExpired(false)
                                    .credentialsExpired(false)
                                    .accountLocked(false)
                                    .build();
                        }));
    }
}
