package Models;

public class Blaster {
    private int id;
    private String Color;
    private int Price;
    private String Name;
    private int Capacity;
    private int Range;
    private String Manufacturer;

    public Blaster(int id, String color, int price, String name, 
                   int capacity, int range, String manufacturer) {
        this.id = id;
        this.Color = color;
        this.Price = price;
        this.Name = name;
        this.Capacity = capacity;
        this.Range = range;
        this.Manufacturer = manufacturer;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getColor() { return Color; }
    public int getPrice() { return Price; }
    public String getName() { return Name; }
    public int getCapacity() { return Capacity; }
    public int getRange() { return Range; }
    public String getManufacturer() { return Manufacturer; }
    
    @Override
    public String toString() {
        return Name + " (" + Manufacturer + ") - " + Price + " credits";
    }
}
