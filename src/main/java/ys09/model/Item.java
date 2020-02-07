package ys09.model;

public class Item {

    private final long id;
    private final String name;
    private final long price;
    private final String details;
    private final String photo;

    public Item(long id, String name, long price, String details, String photo) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.details = details;
        this.photo = photo;

    }

    public long getId() { return this.id; }

    public String getName() { return this.name; }

    public long getPrice() { return this.price; }

    public String getDetails() { return this.details; }

    public String getPhoto() {return this.photo; }

}
