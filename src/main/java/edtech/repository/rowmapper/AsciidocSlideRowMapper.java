package edtech.repository.rowmapper;

import edtech.domain.AsciidocSlide;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AsciidocSlide}, with proper type conversions.
 */
@Service
public class AsciidocSlideRowMapper implements BiFunction<Row, String, AsciidocSlide> {

    private final ColumnConverter converter;

    public AsciidocSlideRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AsciidocSlide} stored in the database.
     */
    @Override
    public AsciidocSlide apply(Row row, String prefix) {
        AsciidocSlide entity = new AsciidocSlide();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setContent(converter.fromRow(row, prefix + "_content", String.class));
        entity.setNotes(converter.fromRow(row, prefix + "_notes", String.class));
        entity.setNum(converter.fromRow(row, prefix + "_num", Integer.class));
        entity.setPresentationId(converter.fromRow(row, prefix + "_presentation_id", Long.class));
        return entity;
    }
}
