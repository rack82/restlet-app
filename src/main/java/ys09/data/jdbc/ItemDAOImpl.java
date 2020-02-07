package ys09.data.jdbc;

import ys09.data.ItemDAO;
import ys09.data.Limits;
import ys09.model.Item;
import java.util.List;
import java.util.Map;

public class ItemDAOImpl implements ItemDAO {

    private final DataAccess dataAccess;

    public ItemDAOImpl(DataAccess dataAccess) { this.dataAccess = dataAccess; }

    @Override
    public List<Item> getItems(Limits limits) {
        List<Item> items = dataAccess.getItems(limits.getStart(), limits.getCount());
        limits.setTotal(dataAccess.countItems());
        //System.out.println("ItemDAO Impleeeeeeee items.count = " + limits.getTotal());
        return items;
    }

    @Override
    public List<Map<String, Object>> getItemsMap(Limits limits, String name, String category) {
        List<Map<String, Object>> items = dataAccess.getItemsMap(limits.getStart(), limits.getCount(), name, category);
        limits.setTotal(dataAccess.countItems());
        //System.out.println("ItemDAO Impleeeeeeee items.count = " + limits.getTotal());
        return items;
    }

    @Override
    public List<Map<String, Object>> getItemsOptions(String username, String email, int option) {
        return dataAccess.getItemsOptions(username, email, option);
    }

    @Override
    public Item getItem(long id) { return dataAccess.getItem(id); }

    @Override
    public int addItem(Item item, String category, String username, String email) { return dataAccess.addItem(item, category, username, email); }

    @Override

    public int PatchItem(int itemId, String username, String email) { return dataAccess.PatchItem(itemId, username, email); }
}
