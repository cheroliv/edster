package edtech.repository.rowmapper;

import edtech.domain.LinkResource;
import edtech.domain.enumeration.DocumentResourceType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link LinkResource}, with proper type conversions.
 */
@Service
public class LinkResourceRowMapper implements BiFunction<Row, String, LinkResource> {

    private final ColumnConverter converter;

    public LinkResourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link LinkResource} stored in the database.
     */
    @Override
    public LinkResource apply(Row row, String prefix) {
        LinkResource entity = new LinkResource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", DocumentResourceType.class));
        entity.setUri(converter.fromRow(row, prefix + "_uri", String.class));
        entity.setTarget(converter.fromRow(row, prefix + "_target", String.class));
        entity.setAsciidocSlideId(converter.fromRow(row, prefix + "_asciidoc_slide_id", Long.class));
        return entity;
    }
}
