package co.com.bancolombia.model.jwt.exception;

import co.com.bancolombia.model.exception.DominioException;

public class TokenInvalidoException extends DominioException {

    private static final int HTTP_STATUS_UNAUTHORIZED = 401;

    public TokenInvalidoException() {
        super("Token invalido", HTTP_STATUS_UNAUTHORIZED);
    }
}
