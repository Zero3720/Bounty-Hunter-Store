package Models;

public class Item {
    private int idItem;
    private int Price;
    private String Name;
    private int Quantity;
    private String Manufacturer;
    private String Description;
    
    public Item(int idItem, int price, String name, int quantity, String manufacturer, String description) {
        this.idItem = idItem;
        Price = price;
        Name = name;
        Quantity = quantity;
        Manufacturer = manufacturer;
        Description = description;
    }

    //Getters and Setters
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    
}
