package Database;

import Models.*;
import java.sql.*;

public class DatabaseInserter {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bounty_hunter_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    // Hunter insertion
    public static void insertHunter(Hunter hunter) throws SQLException {
        String sql = "INSERT INTO `Hunter` (`CFPI`, `Name`, `Credits`, `Affiliation`) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE `Name`=VALUES(`Name`), `Credits`=VALUES(`Credits`), `Affiliation`=VALUES(`Affiliation`)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hunter.getCfpi());
            stmt.setString(2, hunter.getName());
            stmt.setInt(3, hunter.getCredits());
            stmt.setString(4, hunter.getAffiliation());
            stmt.executeUpdate();
        }
    }

    // Bounty insertion (fixed to match Bounty table structure)
    public static void insertBounty(Bounty bounty) throws SQLException {
        String sql = "INSERT INTO `Bounty` (`idRecompensa`, `Category`, `Reward`) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE `Category`=VALUES(`Category`), `Reward`=VALUES(`Reward`)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bounty.getIdRecompensa());
            stmt.setString(2, bounty.getCategory());
            stmt.setInt(3, bounty.getReward());
            stmt.executeUpdate();
        }
    }

    // Assign bounty to hunter (fixed to match Hunter_has_Bounty table)
    public static void assignBountyToHunter(int hunterCFPI, int bountyId) throws SQLException {
        String sql = "INSERT IGNORE INTO `Hunter_has_Bounty` (`Hunter_CFPI`, `Bounty_idRecompensa`) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hunterCFPI);
            stmt.setInt(2, bountyId);
            stmt.executeUpdate();
        }
    }

    // Blaster insertion (fixed to match Blaster table)
    public static void insertBlaster(Blaster blaster) throws SQLException {
        String sql = "INSERT INTO `Blaster` (`idBlaster`, `Color`, `Price`, `Name`, `Capacity`, `Range`, `Manufacturer`) VALUES (?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE `Color`=VALUES(`Color`), `Price`=VALUES(`Price`), `Name`=VALUES(`Name`), `Capacity`=VALUES(`Capacity`), `Range`=VALUES(`Range`), `Manufacturer`=VALUES(`Manufacturer`)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, blaster.getId());
            stmt.setString(2, blaster.getColor());
            stmt.setInt(3, blaster.getPrice());
            stmt.setString(4, blaster.getName());
            stmt.setInt(5, blaster.getCapacity());
            stmt.setInt(6, blaster.getRange());
            stmt.setString(7, blaster.getManufacturer());
            stmt.executeUpdate();
        }
    }

    // Item insertion (fixed to match Item table)
    public static void insertItem(Item item) throws SQLException {
        String sql = "INSERT INTO `Item` (`idItem`, `Price`, `Name`, `Quantity`, `Manufacturer`, `Description`) VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE `Price`=VALUES(`Price`), `Name`=VALUES(`Name`), `Quantity`=VALUES(`Quantity`), `Manufacturer`=VALUES(`Manufacturer`), `Description`=VALUES(`Description`)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getIdItem());
            stmt.setInt(2, item.getPrice());
            stmt.setString(3, item.getName());
            stmt.setInt(4, item.getQuantity());
            stmt.setString(5, item.getManufacturer());
            stmt.setString(6, item.getDescription());
            stmt.executeUpdate();
        }
    }
}