package edtech.service.mapper;

import edtech.domain.AsciidocSlide;
import edtech.domain.LinkResource;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.dto.LinkResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LinkResource} and its DTO {@link LinkResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface LinkResourceMapper extends EntityMapper<LinkResourceDTO, LinkResource> {
    @Mapping(target = "asciidocSlide", source = "asciidocSlide", qualifiedByName = "asciidocSlideId")
    LinkResourceDTO toDto(LinkResource s);

    @Named("asciidocSlideId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsciidocSlideDTO toDtoAsciidocSlideId(AsciidocSlide asciidocSlide);
}
