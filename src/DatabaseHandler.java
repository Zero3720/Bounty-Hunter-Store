
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.*;

public class DatabaseHandler {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bounty_hunter_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "root"; // Change to your MySQL password
    
    private static Connection connection;

    // Initialize database connection when class is first used
    static {
        try {
            // 1. Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Establish connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to Bounty Hunters Guild Database!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    // Hunter Operations -----------------------------------------------------------------
    
    public static Hunter authenticateHunter(int cfpi) {
        String sql = "SELECT * FROM `Caçador` WHERE `CFPI` = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cfpi);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Hunter(
                    rs.getInt("CFPI"),
                    rs.getString("Nome"),
                    rs.getInt("Créditos"),
                    rs.getString("Afiliação")
                );
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return null; // Invalid CFPI
    }
    
    public static void updateHunterCredits(int cfpi, int newCredits) {
        String sql = "UPDATE `Caçador` SET `Créditos` = ? WHERE `CFPI` = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newCredits);
            stmt.setInt(2, cfpi);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Credit update failed: " + e.getMessage());
        }
    }
    
    // Bounty Operations ----------------------------------------------------------------
    
    public static List<Bounty> getActiveBounties(int hunterCFPI) {
        List<Bounty> bounties = new ArrayList<>();
        String sql = "SELECT r.* FROM `Recompensa` r " +
                     "JOIN `Caçador_has_Recompensa` cr ON r.`idRecompensa` = cr.`Recompensa_idRecompensa` " +
                     "WHERE cr.`Caçador_CFPI` = ? AND r.`isActive` = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hunterCFPI);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bounties.add(new Bounty(
                    rs.getInt("idRecompensa"),
                    rs.getString("Categoria"),
                    rs.getInt("Valor")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Bounty fetch error: " + e.getMessage());
        }
        return bounties;
    }
    
    public static int getTotalBountyValue(int hunterCFPI) {
        String sql = "SELECT SUM(`Valor`) AS total FROM `Recompensa` r " +
                     "JOIN `Caçador_has_Recompensa` cr ON r.`idRecompensa` = cr.`Recompensa_idRecompensa` " +
                     "WHERE cr.`Caçador_CFPI` = ? AND r.`isActive` = 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hunterCFPI);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Bounty sum error: " + e.getMessage());
        }
        return 0;
    }
    
    // Equipment Operations ------------------------------------------------------------
    
    public static List<Blaster> getAvailableBlasters() {
        List<Blaster> blasters = new ArrayList<>();
        String sql = "SELECT * FROM `Blaster`";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                blasters.add(new Blaster(
                    rs.getInt("idBlaster"),
                    rs.getString("Cor"),
                    rs.getInt("Preço"),
                    rs.getString("Nome"),
                    rs.getInt("Capacidade"),
                    rs.getInt("Alcance"),
                    rs.getString("Fabricante")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Blaster fetch error: " + e.getMessage());
        }
        return blasters;
    }
    
    public static List<Item> getAvailableItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM `Item`";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                items.add(new Item(
                    rs.getInt("idItem"),
                    rs.getInt("Preço"),
                    rs.getString("Nome"),
                    rs.getInt("Quantidade"),
                    rs.getString("Fabricante"),
                    rs.getString("Descrição")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Item fetch error: " + e.getMessage());
        }
        return items;
    }
    
    // Purchase Operations -------------------------------------------------------------
    
    public static boolean purchaseBlaster(int hunterCFPI, int blasterId) {
        try {
            // Start transaction
            connection.setAutoCommit(false);
            
            // 1. Get blaster price
            int price = getBlasterPrice(blasterId);
            
            // 2. Get hunter credits
            int currentCredits = getHunterCredits(hunterCFPI);
            
            // 3. Check if hunter can afford
            if (currentCredits < price) {
                return false;
            }
            
            // 4. Deduct credits
            updateHunterCredits(hunterCFPI, currentCredits - price);
            
            // 5. Record purchase
            String sql = "INSERT INTO `Blaster_has_Caçador` (`Blaster_idBlaster`, `Caçador_CFPI`) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, blasterId);
                stmt.setInt(2, hunterCFPI);
                stmt.executeUpdate();
            }
            
            // Commit transaction
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Purchase failed: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Auto-commit reset failed: " + e.getMessage());
            }
        }
    }
    
    // Helper Methods ------------------------------------------------------------------
    
    private static int getHunterCredits(int cfpi) throws SQLException {
        String sql = "SELECT `Créditos` FROM `Caçador` WHERE `CFPI` = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cfpi);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("Créditos") : 0;
        }
    }
    
    private static int getBlasterPrice(int blasterId) throws SQLException {
        String sql = "SELECT `Preço` FROM `Blaster` WHERE `idBlaster` = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, blasterId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("Preço") : 0;
        }
    }
}