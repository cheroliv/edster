package edtech.repository.rowmapper;

import edtech.domain.QRCodeResource;
import edtech.domain.enumeration.DocumentResourceType;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link QRCodeResource}, with proper type conversions.
 */
@Service
public class QRCodeResourceRowMapper implements BiFunction<Row, String, QRCodeResource> {

    private final ColumnConverter converter;

    public QRCodeResourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link QRCodeResource} stored in the database.
     */
    @Override
    public QRCodeResource apply(Row row, String prefix) {
        QRCodeResource entity = new QRCodeResource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", DocumentResourceType.class));
        entity.setUri(converter.fromRow(row, prefix + "_uri", String.class));
        entity.setData(converter.fromRow(row, prefix + "_data", String.class));
        entity.setAsciidocSlideId(converter.fromRow(row, prefix + "_asciidoc_slide_id", Long.class));
        return entity;
    }
}
