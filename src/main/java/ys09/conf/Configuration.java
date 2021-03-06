package ys09.conf;

import ys09.data.ItemDAO;
import ys09.data.UserDAO;
import ys09.data.jdbc.DataAccess;
import ys09.data.jdbc.ItemDAOImpl;
import ys09.data.jdbc.UserDAOImpl;

import java.util.Properties;
import java.util.Set;

public class Configuration {

    public static final String[] CONFIG_KEYS = new String[]{"x", "y"};
    private static final Configuration self = new Configuration();

    private final DataAccess dataAccess = new DataAccess();
    private String contextPath = null;
    private Properties props = new Properties();

    private Configuration() {
        System.out.println("constructed config object!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public static Configuration getInstance() {
        return self;
    }

    void setup(String contextPath, Properties props) throws ConfigurationException{
        this.contextPath = contextPath;
        this.props = props;

        try {
            dataAccess.setup(
                getProperty("db.driver"),
                getProperty("db.url"),
                getProperty("db.user"),
                getProperty("db.pass")
            );
        }
        catch(Exception e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getProperty(String name) {
        return getProperty(name, null);
    }

    public String getProperty(String name, String defaultValue) {
        return props.getProperty(name, defaultValue);
    }

    public Set<String> propertyNames() {
        return props.stringPropertyNames();
    }

    public UserDAO getUserDAO() {
        return new UserDAOImpl(dataAccess);
    }

    public ItemDAO getItemDAO() { return new ItemDAOImpl(dataAccess); }
}
