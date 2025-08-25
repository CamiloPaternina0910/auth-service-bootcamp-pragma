package co.com.bancolombia.usecase.exception;

import lombok.extern.java.Log;

public class CorreoElectronicoDuplicadoException extends DominioException{

    private static final int HTTP_STATUS_CONFLICT = 409;

    public CorreoElectronicoDuplicadoException(String correoElectronico){
        super("El correo " + correoElectronico + " ya se encuentra registrado en el sistema.", HTTP_STATUS_CONFLICT);
    }
}
