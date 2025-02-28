package education.cccp.edtech.service.mapper;

import education.cccp.edtech.domain.Presentation;
import education.cccp.edtech.domain.User;
import education.cccp.edtech.service.dto.PresentationDTO;
import education.cccp.edtech.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Presentation} and its DTO {@link PresentationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PresentationMapper extends EntityMapper<PresentationDTO, Presentation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    PresentationDTO toDto(Presentation s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
