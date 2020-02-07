package ys09.data.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ys09.model.User;
import static java.lang.System.out;
import java.sql.ResultSet;
import java.sql.SQLException;

class UserRowMapper implements RowMapper<User>  {

	@Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        long id = rs.getLong("id");
        String email = rs.getString("email");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        String password = rs.getString("password");
        long role = rs.getLong("Owner");

        return new User(id, email, name, surname, password, role);
    }
}