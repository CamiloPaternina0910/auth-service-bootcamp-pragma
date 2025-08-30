package co.com.bancolombia.usecase.usuario.exception;

public class UsuarioDocumentoIdentidadNoEncontrado extends DominioException{
    private static final int HTTP_STATUS_NOT_FOUND = 404;
    public UsuarioDocumentoIdentidadNoEncontrado(String documentoIdentificacion){
        super(
                String.format("Usuario con documento de identificación %s no encontrado.",
                documentoIdentificacion), HTTP_STATUS_NOT_FOUND
        );
    }
}
