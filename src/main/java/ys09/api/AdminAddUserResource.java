package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import ys09.conf.Configuration;
import ys09.data.UserDAO;
import ys09.model.User;

import java.util.HashMap;
import java.util.Map;

public class AdminAddUserResource extends ServerResource {

    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException{

        //we assume that the user with id = 1 is logged in
        long ownerId = 1;

        Map<String, Object> map = new HashMap<>();
        //Create a new restlet form
        Form form = new Form(entity);
        Parameter u = form.get(0);

        Gson gusr = new Gson();
        User usr = gusr.fromJson(u.getName(), User.class);
        User entry = new User(usr.getId(), usr.getEmail(), usr.getName(), usr.getSurname(), usr.getPassword(), usr.getRole());
        User returnUsr = userDAO.getUser(entry.getName(), entry.getPassword());
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String authtoken = headers.getFirstValue("accesstoken");
        User potAdmin = userDAO.getUserToken(authtoken);

        if (potAdmin != null){
            if (potAdmin.getRole() == 1){
                if (returnUsr == null){
                    int result = userDAO.addUser(entry);
                    map.put("user", usr);
                }
                else
                    map.put("user", "already exists");
            }
            else
                throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
        }
        else
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);

        return new JsonMapRepresentation(map);
    }

}
