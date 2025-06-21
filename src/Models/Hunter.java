package Models;

public class Hunter {
    private int cfpi;
    private String Name;
    private int Credits;
    private String Affiliation;

    public Hunter(int cfpi, String name, int credits, String affiliation) {
        this.cfpi = cfpi;
        this.Name = name;
        this.Credits = credits;
        this.Affiliation = affiliation;
    }

    // Getters and setters
    public int getCfpi() {
        return cfpi;
    }

    public void setCfpi(int cfpi) {
        this.cfpi = cfpi;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getCredits() {
        return Credits;
    }

    public void setCredits(int credits) {
        this.Credits = credits;
    }

    public String getAffiliation() {
        return Affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.Affiliation = affiliation;
    }
}
