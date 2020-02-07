package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import ys09.conf.Configuration;
import ys09.data.ItemDAO;
import ys09.model.Item;

import java.util.HashMap;
import java.util.Map;

public class ReturnItemResource extends ServerResource {

    private final ItemDAO itemDAO = Configuration.getInstance().getItemDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Map<String, Object> map = new HashMap<>();

        Form form = new Form(entity);
        Parameter par = form.get(0);

        Gson g = new Gson();
        itemId obj = g.fromJson(par.getName(), itemId.class);
        Item item = itemDAO.getItem(obj.id);
        if (item != null){
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("Price", item.getPrice());
            map.put("Details", item.getDetails());
            map.put("Photo", item.getPhoto());
        }
        else
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

        return new JsonMapRepresentation(map);
    }
    class itemId {
        private long id;
    }
}
