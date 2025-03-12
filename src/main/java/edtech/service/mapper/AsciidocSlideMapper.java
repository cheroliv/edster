package edtech.service.mapper;

import edtech.domain.AsciidocSlide;
import edtech.domain.Presentation;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.dto.PresentationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AsciidocSlide} and its DTO {@link AsciidocSlideDTO}.
 */
@Mapper(componentModel = "spring")
public interface AsciidocSlideMapper extends EntityMapper<AsciidocSlideDTO, AsciidocSlide> {
    @Mapping(target = "presentation", source = "presentation", qualifiedByName = "presentationId")
    AsciidocSlideDTO toDto(AsciidocSlide s);

    @Named("presentationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PresentationDTO toDtoPresentationId(Presentation presentation);
}
