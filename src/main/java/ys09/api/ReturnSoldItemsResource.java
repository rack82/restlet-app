package ys09.api;

import org.restlet.data.Header;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import ys09.conf.Configuration;
import ys09.data.ItemDAO;
import ys09.data.UserDAO;
import ys09.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnSoldItemsResource extends ServerResource {

    private final ItemDAO itemDAO = Configuration.getInstance().getItemDAO();
    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation get() throws ResourceException {
        Map<String, Object> map = new HashMap<>();

        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String authtoken = headers.getFirstValue("accesstoken");
        User potUser = userDAO.getUserToken(authtoken);

        if (potUser != null) {
            List<Map<String, Object>> items = itemDAO.getItemsOptions(potUser.getName(), potUser.getEmail(), -1);
            map.put("results", items);
        }
        else
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);

        return new JsonMapRepresentation(map);
    }
}