package ys09.data.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import ys09.model.Item;
import ys09.model.User;
import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataAccess {

    private static final int MAX_TOTAL_CONNECTIONS = 16;
    private static final int MAX_IDLE_CONNECTIONS   = 8;
    
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setup(String driverClass, String url, String user, String pass) throws SQLException {

        //initialize the data source
	    BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(driverClass);
        bds.setUrl(url);
        bds.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
        bds.setUsername(user);
        bds.setPassword(pass);
        bds.setValidationQuery("SELECT 1");
        bds.setTestOnBorrow(true);
        bds.setDefaultAutoCommit(true);

        //check that everything works OK        
        bds.getConnection().close();

        //initialize the jdbc template utility
        jdbcTemplate = new JdbcTemplate(bds);

        //keep the dataSource for the low-level manual example to function (not actually required)
        dataSource = bds;
    }

    public long countUsers() {
        return jdbcTemplate.queryForObject("select count(*) from user", Long.class);
    }

    public List<User> getUsers(long start, long count) {
        Long[] params = new Long[]{start, count};
        return jdbcTemplate.query("select * from user limit ?, ?", params, new UserRowMapper());
    }

    public int addUser(User usr){
        int row = -1;
        Connection conn = null;

        String query = "insert into user (id, email, name, surname, password, Owner) values (?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[] {usr.getId(), usr.getEmail(), usr.getName(), usr.getSurname(), usr.getPassword(), usr.getRole() };
        int[] types = new int[] { Types.INTEGER, Types.LONGNVARCHAR, Types.LONGNVARCHAR, Types.LONGNVARCHAR, Types.LONGNVARCHAR, Types.INTEGER };

        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            row = jdbcTemplate.update(query, params, types);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return row;
    }

    public User getUser(String userName, String password){

        String tempPassword = generate_hashed_password(password, userName);
        Object[] params = new Object[]{userName, tempPassword};
        System.out.println("name = " + userName + tempPassword);
        //List<User> users = jdbcTemplate.query("select * from user where name = ? and password = ?", params, new UserRowMapper());
        List<User> users = jdbcTemplate.query("select * from user where name = ? and password = ?", params, new UserRowMapper());
        if (users.size() == 1)  {
            return (users.get(0));
        }
        else {
            return null;
        }
    }

    public void assignToken(String token, long id){
        String query = "update user set AccessToken = ? where id = ?";
        Connection conn = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            int row = jdbcTemplate.update(query, token, id);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public User getUserToken(String token){
        String[] params = new String[]{token};
        List<User> users = jdbcTemplate.query("select * from user where AccessToken = ?", params, new UserRowMapper());
        if (users.size() == 1)  {
            return (users.get(0));
        }
        else {
            return null;
        }
    }

    public Optional<User> getUser(Long id) {
        Long[] params = new Long[]{id};
        List<User> users = jdbcTemplate.query("select * from user where password = ?", params, new UserRowMapper());
        if (users.size() == 1)  {
            return Optional.of(users.get(0));
        }
        else {
            return Optional.empty();
        }
    }

    public int SetUserStatus(long id, int status){
        String query = "update user set Status = ? where id = ?";
        int row = -1;
        Connection conn = null;
        Object[] params = new Object[] {status, id};
        int[] types = new int[] {Types.INTEGER, Types.INTEGER};
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            row = jdbcTemplate.update(query, params, types);

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return row;
    }

    public long countItems() {
        return jdbcTemplate.queryForObject("select count(*) from Items", Long.class);
    }

    public List<Item> getItems(long start, long count) {
        Long[] params = new Long[]{start, count};
        return jdbcTemplate.query("select * from Items limit ?, ?", params, new ItemRowMapper());
    }

    public List<Map<String, Object>> getItemsMap(long start, long count, String name, String category) {
        String sql = null;
        Object[] Params = null;
        int[] types = null;

        if (name == null) {
            Params = new Object[]{0};
            types = new int[] {Types.INTEGER};
            sql = "select ItemId, Name, Price, Details, Photo_Target from Items i, Transactions t where i.ItemId = t.Item_id and t.Buy_Sell = ?";
        }
        else {
            Params = new Object[] {category, 0, "%name%"};
            types = new int[] {Types.LONGVARCHAR, Types.INTEGER, Types.LONGVARCHAR};
            sql = "select ItemId, Name, Price, Details, Photo_Target from Items i, Transactions t, Categories c where i.ItemId = t.Item_id and c.Item_id = i.ItemId and c.type = ? and t.Buy_Sell = ? and i.Name Like ?";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, Params, types);

        return list;
    }

    public List<Map<String, Object>> getItemsOptions(String username, String email, int option) {
        String itemsQuery = null;
        Object[] itmParams = null;
        int[] types = null;

        Object[] usrParams = new Object[] {username, email};
        int usrId = jdbcTemplate.queryForObject("select id from user u where u.name = ? and u.email = ?", usrParams, Integer.class);
        if (option == -1) {
            itmParams = new Object[]{usrId, null};
            types = new int[] {Types.LONGVARCHAR, Types.NULL};
            itemsQuery = "select ItemId, Name, Price, Details, Photo_Target from Items i, Transactions t where i.ItemId = t.Item_id and t.user_id = ? and t.Buy_Sell = ?";
        }
        else {
            itmParams = new Object[] {usrId, option};
            types = new int[] {Types.LONGVARCHAR, Types.INTEGER};
            itemsQuery = "select ItemId, Name, Price, Details, Photo_Target from Items i, Transactions t where i.ItemId = t.Item_id and t.user_id = ? and t.Buy_Sell = ?";
        }
        List<Map<String, Object>> items = jdbcTemplate.queryForList(itemsQuery);

        return items;
    }

    public Item getItem(long id) {
        Long[] params = new Long[]{id};
        List<Item> items = jdbcTemplate.query("select * from Items where ItemId = ?", params, new ItemRowMapper());
        if (items.size() == 1)  {
            return (items.get(0));
        }
        else {
            return null;
        }
    }

    public int addItem(Item item, String category, String username, String email) {

        Connection conn = null;
        int Transrow = -1;
        int row = -1;
        int CatRow = -1;
        String query = "insert into Items (Name, Price, Details, Photo_target) values (?, ?, ?, ?)";
        String Transquery = "insert into Transactions (user_id, item_id, Buy_Sell) values (?, ?, ?)";
        String updateCategory = "insert into Categories (Item_id, type) values (?, ?)";
        String findUser = "select id from user where name = ? and email = ?";
        String[] usrparams = new String[] {username, email};
        List<User> usr = jdbcTemplate.query("select * from user where name = ? and email = ?", usrparams, new UserRowMapper());

        int lastId = jdbcTemplate.queryForObject("select max(ItemId) from Items;", Integer.class);
        Object[] params = new Object[] {item.getName(), item.getPrice(), item.getDetails(), item.getPhoto()};
        int[] types = new int[] { Types.LONGNVARCHAR, Types.INTEGER, Types.LONGNVARCHAR, Types.LONGNVARCHAR};

        Object[] TransParams = new Object[] {usr.get(0).getId(), (lastId + 1), 0};
        int[] TransTypes = new int[] {Types.INTEGER, Types.INTEGER, Types.INTEGER};

        Object[] categoryParams = new Object[] { lastId + 1, category};
        int[] CatTypes = new int[] { Types.INTEGER, Types.LONGNVARCHAR };

        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            row = jdbcTemplate.update(query, params, types);
            Transrow = jdbcTemplate.update(Transquery, TransParams, TransTypes);
            CatRow = jdbcTemplate.update(updateCategory, categoryParams, CatTypes);

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return row;
    }


    public int PatchItem(int itemId, String username, String email) {

        Connection conn = null;
        int row = -1;
        int delrow = -1;

        String inQuery = "insert into Transactions (user_id, Item_id, Buy_Sell) values (?, ?, ?)";
        String delQuery = "update Transactions set Buy_Sell = null where Item_id = ? and Buy_Sell = ?";
        String[] usrparams = new String[] {username, email};

        List<User> usr = jdbcTemplate.query("select * from user where name = ? and email = ?", usrparams, new UserRowMapper());

        Object[] params = new Object[] { usr.get(0).getId(), itemId, 1};
        Object[] delParams = new Object[] { itemId, 0};

        int[] types = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER };
        int[] delTypes = new int[] { Types.INTEGER, Types.INTEGER };

        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            row = jdbcTemplate.update(inQuery, params, types);
            delrow = jdbcTemplate.update(delQuery, delParams, delTypes);

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null){
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return row;
    }

    //delete hash functs new object after json parse

    public static String generate_hashed_password(String pre_hashed_password, String string_for_salt) {

        String hash;
        byte[] salt = string_for_salt.getBytes();
        hash = get_SHA_512_SecurePassword(pre_hashed_password, salt);
        return hash;
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}