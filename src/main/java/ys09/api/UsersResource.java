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
import ys09.AuthorizationUtils.UserStruct;
import ys09.conf.Configuration;
import ys09.data.UserDAO;
import ys09.data.Limits;
import ys09.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersResource extends ServerResource {

    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation get() throws ResourceException {

        Limits limits = new Limits(0, 10);
        Map<String, Object> map = new HashMap<>();

        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String authtoken = headers.getFirstValue("accesstoken");
        User user = userDAO.getUserToken(authtoken);

        if ((user != null)) {
            if (user.getRole() == 1) {
                List<User> users = userDAO.getUsers(limits);
                map.put("start", limits.getStart());
                map.put("count", limits.getCount());
                map.put("total", limits.getTotal());
                map.put("results", users);
            }
            else
                throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
        }
        else
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);

        //return a json representation of the newly created record
        return new JsonMapRepresentation(map);
    }
}
