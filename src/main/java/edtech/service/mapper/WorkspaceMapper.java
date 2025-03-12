package edtech.service.mapper;

import edtech.domain.User;
import edtech.domain.Workspace;
import edtech.service.dto.UserDTO;
import edtech.service.dto.WorkspaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Workspace} and its DTO {@link WorkspaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkspaceMapper extends EntityMapper<WorkspaceDTO, Workspace> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    WorkspaceDTO toDto(Workspace s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
