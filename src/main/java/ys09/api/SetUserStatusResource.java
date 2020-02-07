package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import ys09.conf.Configuration;
import ys09.data.UserDAO;
import ys09.model.User;
import ys09.AuthorizationUtils.UserStatusStruct;
import java.util.HashMap;
import java.util.Map;

public class SetUserStatusResource extends ServerResource {

    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Map<String, Object> map = new HashMap<>();
        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        Parameter parameter = form.get(0);

        Gson g = new Gson();
        UserStatusStruct us = g.fromJson(parameter.getName(), UserStatusStruct.class);
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String authtoken = headers.getFirstValue("accesstoken");

        User potAdmin = userDAO.getUserToken(authtoken);
        if (potAdmin != null){
            if (potAdmin.getRole() == 1){
                int result = userDAO.SetUserStatus(us.getId(), us.getStatus());
                map.put("result", "success");
            }
        }
        return new JsonMapRepresentation(map);
    }
}
