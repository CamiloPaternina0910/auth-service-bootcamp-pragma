package co.com.bancolombia.usecase.usuario.exception;

public class CorreoElectronicoDuplicadoException extends DominioException{

    private static final int HTTP_STATUS_BAD_REQUEST = 400;

    public CorreoElectronicoDuplicadoException(String correoElectronico){
        super("El correo " + correoElectronico + " ya se encuentra registrado en el sistema.", HTTP_STATUS_BAD_REQUEST);
    }
}
