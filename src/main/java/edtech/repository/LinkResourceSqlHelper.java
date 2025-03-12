package edtech.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LinkResourceSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("uri", table, columnPrefix + "_uri"));
        columns.add(Column.aliased("target", table, columnPrefix + "_target"));

        columns.add(Column.aliased("asciidoc_slide_id", table, columnPrefix + "_asciidoc_slide_id"));
        return columns;
    }
}
