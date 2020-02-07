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
import ys09.data.ItemDAO;
import ys09.data.UserDAO;
import ys09.model.Item;
import ys09.model.User;

import java.util.HashMap;
import java.util.Map;

public class NewItemResource extends ServerResource {

    private final ItemDAO itemDAO = Configuration.getInstance().getItemDAO();
    private final UserDAO userDAO = Configuration.getInstance().getUserDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException{

        Map<String, Object> map = new HashMap<>();

        //Create a new restlet form
        Form form = new Form(entity);
        Parameter par = form.get(0);

        //Read the parameters
        Gson gObj = new Gson();
        responseItem item = gObj.fromJson(par.getName(), responseItem.class);
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String authtoken = headers.getFirstValue("accesstoken");
        User potUser = userDAO.getUserToken(authtoken);
        //validate the values (in the general case)
        //...
        //System.out.println("Item received!!!!!!!!! = " + item.item.getId() + " " + item.item.getName() + " " + item.item.getPrice() + " " + item.item.getDetails() + " " + item.item.getPhoto());
        //use the DAO machinery to add the new item
        if (potUser != null){
            int result = itemDAO.addItem(item.item, item.Category, potUser.getName(), potUser.getEmail());
        }
        else
            throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
        map.put("item", item);


        //return a json representation of the newly created record
        return new JsonMapRepresentation(map);
    }
    class responseItem {
        private Item item;
        private String Category;
    }
}
