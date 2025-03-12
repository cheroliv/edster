package edtech.repository.rowmapper;

import edtech.domain.PlantUMLDiagramResource;
import edtech.domain.enumeration.DocumentResourceType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PlantUMLDiagramResource}, with proper type conversions.
 */
@Service
public class PlantUMLDiagramResourceRowMapper implements BiFunction<Row, String, PlantUMLDiagramResource> {

    private final ColumnConverter converter;

    public PlantUMLDiagramResourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PlantUMLDiagramResource} stored in the database.
     */
    @Override
    public PlantUMLDiagramResource apply(Row row, String prefix) {
        PlantUMLDiagramResource entity = new PlantUMLDiagramResource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", DocumentResourceType.class));
        entity.setUri(converter.fromRow(row, prefix + "_uri", String.class));
        entity.setUmlCode(converter.fromRow(row, prefix + "_uml_code", String.class));
        entity.setAsciidocSlideId(converter.fromRow(row, prefix + "_asciidoc_slide_id", Long.class));
        return entity;
    }
}
