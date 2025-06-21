package Models;

public class Bounty {
    private int idRecompensa;
    private String Category;
    private int Reward;

    public Bounty(int idRecompensa, String category, int reward) {
        this.idRecompensa = idRecompensa;
        Category = category;
        Reward = reward;
    }
    
    //Getters and Setters
    public int getIdRecompensa() {
        return idRecompensa;
    }
    public void setIdRecompensa(int idRecompensa) {
        this.idRecompensa = idRecompensa;
    }
    public String getCategory() {
        return Category;
    }
    public void setCategory(String category) {
        Category = category;
    }
    public int getReward() {
        return Reward;
    }
    public void setReward(int reward) {
        Reward = reward;
    }

}
