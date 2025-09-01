package co.com.bancolombia.r2dbc.rol.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {

    @Id
    private String id;

    private String nombre;

    private String descripcion;
}
