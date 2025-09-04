package co.com.bancolombia.model.usuario.exception;

import co.com.bancolombia.model.exception.DominioException;

public class RolPorNombreNoEncontradoException extends DominioException {

    private static final int HTTP_STATUS_NOT_FOUND = 404;
    public RolPorNombreNoEncontradoException(String nombre){
        super(
                String.format("El rol con el nombre %s no fue encontrado.", nombre),
                HTTP_STATUS_NOT_FOUND
        );
    }
}
