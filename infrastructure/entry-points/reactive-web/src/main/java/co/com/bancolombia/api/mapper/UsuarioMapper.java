package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.usuario.dto.CrearUsuarioDto;
import co.com.bancolombia.api.usuario.dto.LecturaUsuarioDto;
import co.com.bancolombia.api.usuario.dto.EditarUsuarioDto;
import co.com.bancolombia.model.usuario.Usuario;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    LecturaUsuarioDto toResponse(Usuario usuario);
    List<LecturaUsuarioDto> toResponse(List<Usuario> usuarios);

    Usuario toModel(CrearUsuarioDto userDto);

    Usuario toModel(EditarUsuarioDto userDto);
}
