package edtech.service.mapper;

import edtech.domain.Presentation;
import edtech.domain.Workspace;
import edtech.service.dto.PresentationDTO;
import edtech.service.dto.WorkspaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Presentation} and its DTO {@link PresentationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PresentationMapper extends EntityMapper<PresentationDTO, Presentation> {
    @Mapping(target = "workspace", source = "workspace", qualifiedByName = "workspaceId")
    PresentationDTO toDto(Presentation s);

    @Named("workspaceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WorkspaceDTO toDtoWorkspaceId(Workspace workspace);
}
