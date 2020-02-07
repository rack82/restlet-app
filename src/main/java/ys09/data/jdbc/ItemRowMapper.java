package ys09.data.jdbc;

import ys09.model.Item;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

class ItemRowMapper implements RowMapper<Item>  {

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id = rs.getLong("ItemId");
        String name = rs.getString("Name");
        long price = rs.getLong("Price");
        String details = rs.getString("Details");
        String photo = rs.getString("Photo_target");

        return new Item(id, name, price, details, photo);
    }
}
