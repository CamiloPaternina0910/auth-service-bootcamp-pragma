package co.com.bancolombia.model.usuario.exception;

import co.com.bancolombia.model.exception.DominioException;

public class SalarioInvalidoException extends DominioException {

    private static final int HTTP_STATUS_BAD_REQUEST = 404;

    public SalarioInvalidoException() {
        super("El salario base del empleado debería estar entre $ 0 y $ 15.000.000.", HTTP_STATUS_BAD_REQUEST);
    }
}
