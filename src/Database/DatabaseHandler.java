package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Models.*;

public class DatabaseHandler {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bounty_hunter_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

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

    // Hunter Operations
    // -----------------------------------------------------------------

    public static Hunter authenticateHunter(int cfpi) {
        String sql = "SELECT * FROM `Hunter` WHERE `CFPI` = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cfpi);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Hunter(
                        rs.getInt("CFPI"),
                        rs.getString("Name"),
                        rs.getInt("Credits"),
                        rs.getString("Affiliation"));
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return null; // Invalid CFPI
    }

    public static void updateHunterCredits(int cfpi, int newCredits) {
        String sql = "UPDATE `Hunter` SET `Credits` = ? WHERE `CFPI` = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newCredits);
            stmt.setInt(2, cfpi);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Credit update failed: " + e.getMessage());
        }
    }

    // Bounty Operations
    // ----------------------------------------------------------------

    public static List<Bounty> getActiveBounties(int hunterCFPI) {
        List<Bounty> bounties = new ArrayList<>();
        // Updated to match new table/column names
        String sql = "SELECT b.* FROM `Bounty` b " +
                "JOIN `Hunter_has_Bounty` hb ON b.`idRecompensa` = hb.`Bounty_idRecompensa` " +
                "WHERE hb.`Hunter_CFPI` = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hunterCFPI);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bounties.add(new Bounty(
                        rs.getInt("idRecompensa"),
                        rs.getString("Category"),
                        rs.getInt("Reward")));
            }
        } catch (SQLException e) {
            System.err.println("Bounty fetch error: " + e.getMessage());
        }
        return bounties;
    }

    public static int getTotalBountyValue(int hunterCFPI) {
        // Updated to match new table/column names
        String sql = "SELECT SUM(`Reward`) AS total FROM `Bounty` b " +
                "JOIN `Hunter_has_Bounty` hb ON b.`idRecompensa` = hb.`Bounty_idRecompensa` " +
                "WHERE hb.`Hunter_CFPI` = ?";

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

    // Equipment Operations
    // ------------------------------------------------------------

    public static List<Blaster> getAvailableBlasters() {
        List<Blaster> blasters = new ArrayList<>();
        String sql = "SELECT * FROM `Blaster`";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                blasters.add(new Blaster(
                        rs.getInt("idBlaster"),
                        rs.getString("Color"),
                        rs.getInt("Price"),
                        rs.getString("Name"),
                        rs.getInt("Capacity"),
                        rs.getInt("Range"),
                        rs.getString("Manufacturer")));
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
                        rs.getInt("Price"),
                        rs.getString("Name"),
                        rs.getInt("Quantity"),
                        rs.getString("Manufacturer"),
                        rs.getString("Description")));
            }
        } catch (SQLException e) {
            System.err.println("Item fetch error: " + e.getMessage());
        }
        return items;
    }

    // Purchase Operations
    // -------------------------------------------------------------

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

    public static boolean purchaseItem(int hunterCFPI, int itemId) {
        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Get item price
            int price = getItemPrice(itemId);

            // 2. Get hunter credits
            int currentCredits = getHunterCredits(hunterCFPI);

            // 3. Check if hunter can afford
            if (currentCredits < price) {
                return false;
            }

            // 4. Deduct credits
            updateHunterCredits(hunterCFPI, currentCredits - price);

            // 5. Record purchase
            String sql = "INSERT INTO `Item_has_Caçador` (`Item_idItem`, `Caçador_CFPI`) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, itemId);
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
            System.err.println("Item purchase failed: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Auto-commit reset failed: " + e.getMessage());
            }
        }
    }

    // Helper Methods
    // ------------------------------------------------------------------

    private static int getHunterCredits(int cfpi) throws SQLException {
        // Updated to match new table/column names
        String sql = "SELECT `Credits` FROM `Hunter` WHERE `CFPI` = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cfpi);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("Credits") : 0;
        }
    }

    private static int getBlasterPrice(int blasterId) throws SQLException {
        // Updated to match new column name
        String sql = "SELECT `Price` FROM `Blaster` WHERE `idBlaster` = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, blasterId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("Price") : 0;
        }
    }

    private static int getItemPrice(int itemId) throws SQLException {
        String sql = "SELECT `Price` FROM `Item` WHERE `idItem` = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("Price") : 0;
        }
    }

    public static boolean doesHunterOwnBlaster(int hunterCFPI, int blasterId) {
        String query = "SELECT COUNT(*) FROM `Blaster_has_Caçador` " +
                "WHERE `Blaster_idBlaster` = ? AND `Caçador_CFPI` = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, blasterId);
            pstmt.setInt(2, hunterCFPI);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if count > 0
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking blaster ownership: " + e.getMessage());
        }
        return false;
    }

    public static boolean doesHunterOwnItem(int hunterCFPI, int itemId) {
        String query = "SELECT COUNT(*) FROM `Item_has_Caçador` " +
                "WHERE `Item_idItem` = ? AND `Caçador_CFPI` = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, hunterCFPI);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking item ownership: " + e.getMessage());
        }
        return false;
    }

    // Cleaner Methods
    // ------------------------------------------------------------------
    public static boolean resetDatabase() {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("Starting database reset...");

            // Disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            System.out.println("Foreign key checks disabled");

            // Truncate tables
            truncateTable(stmt, "Blaster_has_Caçador");
            truncateTable(stmt, "Item_has_Caçador");
            truncateTable(stmt, "Hunter_has_Bounty");
            truncateTable(stmt, "Blaster");
            truncateTable(stmt, "Bounty");
            truncateTable(stmt, "Hunter");
            truncateTable(stmt, "Item");

            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("Foreign key checks re-enabled");

            System.out.println("Database reset complete!");
            return true;
        } catch (SQLException e) {
            System.err.println("Database reset failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void truncateTable(Statement stmt, String tableName) throws SQLException {
        System.out.println("Truncating table: " + tableName);
        stmt.execute("TRUNCATE TABLE `" + tableName + "`");
        System.out.println("Table " + tableName + " truncated successfully");
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}