package xyz.larkyy.aquaticlibrary.database;
import java.util.List;

public class Table {

    private final String name;
    private final List<Column> columns;
    private final Column primaryColumn = null;

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    public String getInsertSql() {
        String columnsString = buildColumnsString();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.stream().filter(c -> !c.isPrimary()).count(); i++) {
            sb.append("?");
            sb.append(",");
        }
        var valuesString = sb.toString();
        valuesString = valuesString.substring(0,valuesString.length()-1);
        var sql = "INSERT INTO "+name+" ("+columnsString+") VALUES ("+valuesString+")";
        return sql;
    }

    private String buildColumnsString() {
        StringBuilder sb = new StringBuilder();
        for (var column : columns) {
            if (!column.isPrimary()) {
                sb.append(column.getName());
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public String getDeleteSql() {
        return "";
    }

    public String getUpdageSql() {
        return "";
    }

    public String getSelectSql() {
        return "";
    }

    public Column getPrimaryColumn() {
        return primaryColumn;
    }
}
