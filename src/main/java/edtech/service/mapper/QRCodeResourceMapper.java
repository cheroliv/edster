package edtech.service.mapper;

import edtech.domain.AsciidocSlide;
import edtech.domain.QRCodeResource;
import edtech.service.dto.AsciidocSlideDTO;
import edtech.service.dto.QRCodeResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QRCodeResource} and its DTO {@link QRCodeResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface QRCodeResourceMapper extends EntityMapper<QRCodeResourceDTO, QRCodeResource> {
    @Mapping(target = "asciidocSlide", source = "asciidocSlide", qualifiedByName = "asciidocSlideId")
    QRCodeResourceDTO toDto(QRCodeResource s);

    @Named("asciidocSlideId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AsciidocSlideDTO toDtoAsciidocSlideId(AsciidocSlide asciidocSlide);
}
