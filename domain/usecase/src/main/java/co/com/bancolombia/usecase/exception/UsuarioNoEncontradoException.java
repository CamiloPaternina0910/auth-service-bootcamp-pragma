package co.com.bancolombia.usecase.exception;

public class UsuarioNoEncontradoException extends DominioException {

    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public UsuarioNoEncontradoException(String correoElectronico) {
        super("Usuario con el correo " + correoElectronico + " no encontrado.", HTTP_STATUS_NOT_FOUND);
    }
}
