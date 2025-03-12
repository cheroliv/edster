package edtech.service.mapper;

import edtech.domain.AsciidocSlide;
import edtech.domain.PlantUMLDiagramResource;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.dto.PlantUMLDiagramResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlantUMLDiagramResource} and its DTO {@link PlantUMLDiagramResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlantUMLDiagramResourceMapper extends EntityMapper<PlantUMLDiagramResourceDTO, PlantUMLDiagramResource> {
    @Mapping(target = "asciidocSlide", source = "asciidocSlide", qualifiedByName = "asciidocSlideId")
    PlantUMLDiagramResourceDTO toDto(PlantUMLDiagramResource s);

    @Named("asciidocSlideId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsciidocSlideDTO toDtoAsciidocSlideId(AsciidocSlide asciidocSlide);
}
