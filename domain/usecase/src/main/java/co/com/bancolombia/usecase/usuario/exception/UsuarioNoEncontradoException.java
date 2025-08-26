package co.com.bancolombia.usecase.usuario.exception;

import java.math.BigInteger;

public class UsuarioNoEncontradoException extends DominioException {

    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public UsuarioNoEncontradoException(String id) {
        super("Usuario con id " + id + " no encontrado.", HTTP_STATUS_NOT_FOUND);
    }
}
