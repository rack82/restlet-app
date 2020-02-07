package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Header;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import ys09.AuthorizationUtils.ItemStruct;
import ys09.conf.Configuration;
import ys09.data.ItemDAO;
import ys09.data.UserDAO;
import ys09.model.Item;
import ys09.model.User;

import java.util.HashMap;
import java.util.Map;

public class BuyItemResource  extends ServerResource {

    private final ItemDAO itemDAO = Configuration.getInstance().getItemDAO();
    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation patch(Representation entity) throws ResourceException {

        Map<String, Object> map = new HashMap<>();

        Form form = new Form(entity);

        Parameter par = form.get(0);
        Gson g = new Gson();
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String authtoken = headers.getFirstValue("accesstoken");
        ItemStruct response = g.fromJson(par.getName(), ItemStruct.class);
        User usr = userDAO.getUserToken(authtoken);

        if (usr != null){
            int row = itemDAO.PatchItem(response.getItemId(), usr.getName(), usr.getEmail());
            map.put("payload item", response.getItemId());
        }
        else
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);

        return new JsonMapRepresentation(map);
    }
}
