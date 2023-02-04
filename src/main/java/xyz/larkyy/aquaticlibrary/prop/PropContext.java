package xyz.larkyy.aquaticlibrary.prop;

import com.google.gson.Gson;
import org.bukkit.Location;
import xyz.larkyy.aquaticlibrary.AquaticLibrary;
import xyz.larkyy.aquaticlibrary.database.DatabaseContext;
import xyz.larkyy.aquaticlibrary.database.configuration.DatabaseConfiguration;
import xyz.larkyy.aquaticlibrary.database.configuration.SQLiteAdapter;
import xyz.larkyy.aquaticlibrary.reflection.ReflectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PropContext<T extends Prop> extends DatabaseContext {

    private final Function<Map.Entry<Location,PropData>,T> createProp;
    private final String tableName;
    private final Consumer<DatabaseConfiguration> onConfiguring;

    public PropContext(String tableName,Function<Map.Entry<Location,PropData>,T> createPropFunction, Consumer<DatabaseConfiguration> onConfiguring) {
        this.createProp = createPropFunction;
        this.tableName = tableName;
        this.onConfiguring = onConfiguring;
    }

    public void addProp(T prop) {
        getDatabase().sql("INSERT INTO "+tableName+" (location,data) VALUES (?,?)",pstmt -> {
            try {
                var gson = new Gson();
                pstmt.setObject(1, gson.toJson(prop.getLocation().serialize()));
                pstmt.setObject(2, gson.toJson(prop.getPropData().getData()));

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    return;
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long key = generatedKeys.getLong(1);
                        prop.getPropData().addData("table_column_id",key+"");
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateProp(T prop) {
        //props.update(prop);
    }

    public void deleteProp(T prop) {
        getDatabase().sql("DELETE FROM "+tableName+" WHERE id = ?", pstmt -> {
            try {
                var idStr = prop.getPropData().getData("table_column_id");
                if (idStr == null) {
                    return;
                }
                var value = Long.parseLong(idStr);
                pstmt.setLong(1,value);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<T> loadProps() {
        final List<T> props = new ArrayList<>();

        final var gson = new Gson();
        getDatabase().sql("SELECT * FROM "+tableName, pstmt -> {
            try {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Location location = Location.deserialize(gson.fromJson(rs.getString("location"), Map.class));
                    PropData propData = new PropData(gson.fromJson(rs.getString("data"), Map.class));
                    var prop = createProp.apply(new AbstractMap.SimpleEntry<>(location,propData));
                    props.add(prop);
                    prop.load();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return props;
    }

    @Override
    public void onConfiguring(DatabaseConfiguration configuration) {
        onConfiguring.accept(configuration);
    }

    @Override
    public void setup() throws Exception {
        super.setup();

        getDatabase().sql("CREATE TABLE IF NOT EXISTS "+tableName+" (id integer PRIMARY KEY, location text NOT NULL, data text NOT NULL)", pstmt -> {
            try {
                pstmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
