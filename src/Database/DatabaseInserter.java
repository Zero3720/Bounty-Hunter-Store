package Database;

import Models.*;
import java.sql.*;

public class DatabaseInserter {
    // Reuse your existing DB connection details
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
        String sql = "INSERT INTO `Caçador` (`CFPI`, `Nome`, `Créditos`, `Afiliação`) VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE `Nome`=VALUES(`Nome`), `Créditos`=VALUES(`Créditos`), `Afiliação`=VALUES(`Afiliação`)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hunter.getCfpi());
            stmt.setString(2, hunter.getName());
            stmt.setInt(3, hunter.getCredits());
            stmt.setString(4, hunter.getAffiliation());
            stmt.executeUpdate();
        }
    }

    // Bounty insertion (with active status)
    public static void insertBounty(Bounty bounty, boolean isActive) throws SQLException {
        String sql = "INSERT INTO `Recompensa` (`idRecompensa`, `Categoria`, `Valor`, `isActive`) VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE `Categoria`=VALUES(`Categoria`), `Valor`=VALUES(`Valor`), `isActive`=VALUES(`isActive`)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bounty.getIdRecompensa());
            stmt.setString(2, bounty.getCategory());
            stmt.setInt(3, bounty.getReward());
            stmt.setBoolean(4, isActive);
            stmt.executeUpdate();
        }
    }

    // Assign bounty to hunter
    public static void assignBountyToHunter(int hunterCFPI, int bountyId) throws SQLException {
        String sql = "INSERT IGNORE INTO `Caçador_has_Recompensa` (`Caçador_CFPI`, `Recompensa_idRecompensa`) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hunterCFPI);
            stmt.setInt(2, bountyId);
            stmt.executeUpdate();
        }
    }

    // Blaster insertion
    public static void insertBlaster(Blaster blaster) throws SQLException {
        String sql = "INSERT INTO `Blaster` (`idBlaster`, `Cor`, `Preço`, `Nome`, `Capacidade`, `Alcance`, `Fabricante`) VALUES (?, ?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE `Cor`=VALUES(`Cor`), `Preço`=VALUES(`Preço`), `Nome`=VALUES(`Nome`), `Capacidade`=VALUES(`Capacidade`), `Alcance`=VALUES(`Alcance`), `Fabricante`=VALUES(`Fabricante`)";
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

    // Item insertion
    public static void insertItem(Item item) throws SQLException {
        String sql = "INSERT INTO `Item` (`idItem`, `Preço`, `Nome`, `Quantidade`, `Fabricante`, `Descrição`) VALUES (?, ?, ?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE `Preço`=VALUES(`Preço`), `Nome`=VALUES(`Nome`), `Quantidade`=VALUES(`Quantidade`), `Fabricante`=VALUES(`Fabricante`), `Descrição`=VALUES(`Descrição`)";
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