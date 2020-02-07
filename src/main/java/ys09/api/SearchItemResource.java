package ys09.api;

import com.google.gson.Gson;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import ys09.conf.Configuration;
import ys09.data.ItemDAO;
import ys09.data.Limits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchItemResource extends ServerResource {

    private final ItemDAO itemDAO = Configuration.getInstance().getItemDAO();

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        Limits limits = new Limits(0, 20);
        Form form = new Form(entity);
        Parameter parameter = form.get(0);
        Gson g = new Gson();
        SearchItemObject item = g.fromJson(parameter.getName(), SearchItemObject.class);
        List<Map<String, Object>> itemsMap = itemDAO.getItemsMap(limits, item.name, item.Category);

        Map<String, Object> map = new HashMap<>();
        map.put("start", limits.getStart());
        map.put("count", limits.getCount());
        map.put("total", limits.getTotal());
        map.put("results", itemsMap);

        return new JsonMapRepresentation(map);

    }
    class SearchItemObject{
        private String name;
        private String Category;
    }
}
