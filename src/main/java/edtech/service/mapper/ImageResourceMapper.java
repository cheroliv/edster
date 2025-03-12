package edtech.service.mapper;

import edtech.domain.AsciidocSlide;
import edtech.domain.ImageResource;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.dto.ImageResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImageResource} and its DTO {@link ImageResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageResourceMapper extends EntityMapper<ImageResourceDTO, ImageResource> {
    @Mapping(target = "asciidocSlide", source = "asciidocSlide", qualifiedByName = "asciidocSlideId")
    ImageResourceDTO toDto(ImageResource s);

    @Named("asciidocSlideId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsciidocSlideDTO toDtoAsciidocSlideId(AsciidocSlide asciidocSlide);
}
