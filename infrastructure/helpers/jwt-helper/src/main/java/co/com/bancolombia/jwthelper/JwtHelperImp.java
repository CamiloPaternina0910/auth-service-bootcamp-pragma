package co.com.bancolombia.jwthelper;

import co.com.bancolombia.model.jwt.gateways.JwtHelper;
import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.model.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtHelperImp implements JwtHelper {

    private static final String AUTHORITIES = "authorization";

    @Value("${jwt.secret:aa2d5cdbf2749b9b0bcf1c144b5b3d11e06a87d467f9490303f5bdc8f4572cd27e5f04db4b10ce371c7de84ddf75cd0c3bc0f2f97e540c4079019bb9d216cadb}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    private final RolRepository rolRepository;

    public Mono<String> generarToken(Usuario usuario) {
        log.info("Generando token para el usuario: {} ...", usuario.getCorreoElectronico());
        log.info("Buscando rol del usuario ...");
        return rolRepository.findById(usuario.getIdRol())
                .map(rol -> {
                    log.info("Rol encontrado: {}", rol.getNombre());
                    final Date now = new Date();
                    final Date expirationDate = new Date(now.getTime() + expiration);
                    List<String> roles = new ArrayList<>();
                    roles.add(rol.getNombre());

                    String token = Jwts.builder()
                            .subject(usuario.getCorreoElectronico())
                            .claims(getClaimsFromUsuario(usuario))
                            .claim(AUTHORITIES, roles)
                            .issuedAt(now)
                            .expiration(expirationDate)
                            .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                            .compact();

                    log.info("Token generado con exito.");
                    return token;
                });
    }

    private Map<String, Object> getClaimsFromUsuario(Usuario usuario) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("nombres", usuario.getNombres());
        claims.put("apellidos", usuario.getApellidos());
        claims.put("id", usuario.getId());
        claims.put("correoElectronico", usuario.getCorreoElectronico());
        claims.put("documentoIdentificacion", usuario.getDocumentoIdentificacion());
        claims.put("telefono", usuario.getTelefono());
        claims.put("fechaNacimiento", usuario.getFechaNacimiento().toString());
        claims.put("salarioBase", usuario.getSalarioBase());

        return claims;
    }

    @SuppressWarnings("unchecked")
    public Mono<List<String>> getRolesDelToken(String jwt) {
        Claims claims = getClaimsFromToke(jwt);
        List<String> claimsString = claims.get(AUTHORITIES, List.class);

        if (claimsString == null) {
            return Mono.empty();
        }
        return Mono.just(claimsString);
    }

    public Mono<String> getUsernameDelToken(String jwt) {
        return Mono.just(getClaimsFromToke(jwt).getSubject());
    }


    private Claims getClaimsFromToke(String jwt) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
