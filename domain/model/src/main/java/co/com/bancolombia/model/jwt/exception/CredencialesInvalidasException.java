package co.com.bancolombia.model.jwt.exception;

import co.com.bancolombia.model.exception.DominioException;

public class CredencialesInvalidasException extends DominioException {

    private static final int HTTP_STATUS_UNAUTHORIZED = 401;

    public CredencialesInvalidasException() {
        super("El correo o la clave son incorrectos.", HTTP_STATUS_UNAUTHORIZED);
    }
}
