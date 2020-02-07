package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import ys09.conf.Configuration;
import ys09.data.UserDAO;
import ys09.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserResource extends ServerResource {

    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException{

        Map<String, Object> map = new HashMap<>();

        //Create a new restlet form
        Form form = new Form(entity);
        Parameter par = form.get(0);
        //Read the parameters

        Gson gObj = new Gson();
        User usr = gObj.fromJson(par.getName(), User.class);
        User newEntry = new User(usr.getId(), usr.getEmail(), usr.getName(), usr.getSurname(), usr.getPassword(), usr.getRole());
        //use the DAO machinery to add the new user
        int result = userDAO.addUser(newEntry);
        if (result != -1)
            map.put("user", newEntry);
        else
            throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE);

        return new JsonMapRepresentation(map);

    }
}
