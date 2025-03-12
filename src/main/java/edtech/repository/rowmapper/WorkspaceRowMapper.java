package edtech.repository.rowmapper;

import edtech.domain.Workspace;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Workspace}, with proper type conversions.
 */
@Service
public class WorkspaceRowMapper implements BiFunction<Row, String, Workspace> {

    private final ColumnConverter converter;

    public WorkspaceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Workspace} stored in the database.
     */
    @Override
    public Workspace apply(Row row, String prefix) {
        Workspace entity = new Workspace();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setPromptSystemMessage(converter.fromRow(row, prefix + "_prompt_system_message", String.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", Long.class));
        return entity;
    }
}
