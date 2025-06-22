package Database;

import java.sql.SQLException;

import Models.*;

public class DatabaseInitializer {
    public static void Setup() {
        System.out.println("Initializing bounty hunter database...");
        
        try {
            // Create sample hunters
            DatabaseInserter.insertHunter(new Hunter(92257797, "Boba Fett", 5000, "Mandalorians"));
            DatabaseInserter.insertHunter(new Hunter(60898790, "Din Djarin", 75000, "Mandalorians"));
            DatabaseInserter.insertHunter(new Hunter(80983913, "Cad Bane", 3000, "None"));
            DatabaseInserter.insertHunter(new Hunter(76017029, "IG-88", 6000, "None"));
            DatabaseInserter.insertHunter(new Hunter(55946112, "Bossk", 4000, "Trandoshan Hunters"));
            DatabaseInserter.insertHunter(new Hunter(10000000, "System Test", 999999, "None"));

            // Create bounties
            DatabaseInserter.insertBounty(new Bounty(1, "Jedi", 50000));
            DatabaseInserter.insertBounty(new Bounty(2, "Smuggler", 15000));
            DatabaseInserter.insertBounty(new Bounty(3, "War Criminal", 30000));

            // Assign bounties to hunters
            DatabaseInserter.assignBountyToHunter(60898790, 1);
            DatabaseInserter.assignBountyToHunter(60898790, 2);
            DatabaseInserter.assignBountyToHunter(00000001, 3);

            // Create blasters
            DatabaseInserter.insertBlaster(new Blaster(1, "Black", 2500, "DL-44", 50, 30, "BlasTech"));
            DatabaseInserter.insertBlaster(new Blaster(2, "Silver", 3500, "EE-3", 100, 100, "Merr-Sonn"));
            DatabaseInserter.insertBlaster(new Blaster(3, "Green", 1800, "S-5", 75, 60, "SoroSuub"));

            // Create equipment items
            DatabaseInserter.insertItem(new Item(1, 500, "Grappling Hook", 1, "Merr-Sonn", "Tibanna-gas propelled"));
            DatabaseInserter.insertItem(new Item(2, 300, "Thermal Detonator", 3, "SoroSuub", "Class-A thermal explosive"));
            DatabaseInserter.insertItem(new Item(3, 1200, "Mandalorian Vambraces", 1, "MandalArms", "Beskar alloy plating"));
            
            System.out.println("Bounty hunter database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}