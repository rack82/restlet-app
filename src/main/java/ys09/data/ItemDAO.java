package ys09.data;

import ys09.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemDAO {

    List<Item> getItems(Limits limits);

    List<Map<String, Object>> getItemsMap(Limits limits, String name, String category);
    List<Map<String, Object>> getItemsOptions(String username, String email, int option);

    Item getItem(long id);

    int addItem(Item item, String category, String username, String email);

    int PatchItem(int itemId, String username, String email);

}
