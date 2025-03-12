package edtech.repository.rowmapper;

import edtech.domain.Presentation;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Presentation}, with proper type conversions.
 */
@Service
public class PresentationRowMapper implements BiFunction<Row, String, Presentation> {

    private final ColumnConverter converter;

    public PresentationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Presentation} stored in the database.
     */
    @Override
    public Presentation apply(Row row, String prefix) {
        Presentation entity = new Presentation();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPlan(converter.fromRow(row, prefix + "_plan", String.class));
        entity.setUri(converter.fromRow(row, prefix + "_uri", String.class));
        entity.setPromptUserMessage(converter.fromRow(row, prefix + "_prompt_user_message", String.class));
        entity.setWorkspaceId(converter.fromRow(row, prefix + "_workspace_id", Long.class));
        return entity;
    }
}
