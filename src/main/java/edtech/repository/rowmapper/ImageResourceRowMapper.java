package edtech.repository.rowmapper;

import edtech.domain.ImageResource;
import edtech.domain.enumeration.DocumentResourceType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link ImageResource}, with proper type conversions.
 */
@Service
public class ImageResourceRowMapper implements BiFunction<Row, String, ImageResource> {

    private final ColumnConverter converter;

    public ImageResourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ImageResource} stored in the database.
     */
    @Override
    public ImageResource apply(Row row, String prefix) {
        ImageResource entity = new ImageResource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", DocumentResourceType.class));
        entity.setUri(converter.fromRow(row, prefix + "_uri", String.class));
        entity.setResolution(converter.fromRow(row, prefix + "_resolution", String.class));
        entity.setAsciidocSlideId(converter.fromRow(row, prefix + "_asciidoc_slide_id", Long.class));
        return entity;
    }
}
