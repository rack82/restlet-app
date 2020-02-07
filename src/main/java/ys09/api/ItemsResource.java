package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import ys09.conf.Configuration;
import ys09.data.ItemDAO;
import ys09.data.Limits;
import ys09.data.UserDAO;
import ys09.model.Item;
import ys09.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsResource extends ServerResource {

    private final ItemDAO itemDAO = Configuration.getInstance().getItemDAO();

    @Override
    protected Representation get() throws ResourceException {
        Limits limits = new Limits(0,20);
        //List<Item> items = itemDAO.getItems(limits);
        List<Map<String, Object>> itemsMap = itemDAO.getItemsMap(limits, null, null);

        Map<String, Object> map = new HashMap<>();
        map.put("start", limits.getStart());
        map.put("count", limits.getCount());
        map.put("total", limits.getTotal());
        map.put("results", itemsMap);

        return new JsonMapRepresentation(map);
    }
}
