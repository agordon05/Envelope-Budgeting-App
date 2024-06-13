package data;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//import dataAccess.BalanceAccess;
//import dataAccess.EnvelopeAccess;
//import dataAccess.StatementAccess;
//import dataAccess.VendorAccess;
//import dataObjects.Balance;
import dataObjects.Envelope;

/**
 * Utility class for interacting with a SQLite database.
 */
public class Database {

    private static final String DB_FOLDER = "data"; // Folder within the project
    private static final String DB_FILE = "envelopes.db"; // Database file name

    // URL to connect to the SQLite database (local path based on project directory)
    private static final String URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/src/" + DB_FOLDER + "/" + DB_FILE;

    public static void Initialize() {
    	createNewDatabase();
//		EnvelopeAccess.Initialize();
//		StatementAccess.Initialize();
//		BalanceAccess.Initialize();
//		VendorAccess.Initialize();
//		BalanceAccess.addBalance(new Balance(getBalance()));
    }
    
//    public static void loadEnvelopes() {
//        String sql = "SELECT * FROM envelopes";
//
//        try (Connection conn = DriverManager.getConnection(URL);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                int priority = rs.getInt("priority");
//                String name = rs.getString("name");
//                BigDecimal amount = new BigDecimal(rs.getString("amount")); // Retrieve as String and convert to BigDecimal
//                int fillSetting = rs.getInt("fill_setting");
//                int fillAmount = rs.getInt("fill_amount");
//                boolean cap = rs.getBoolean("cap");
//                int capAmount = rs.getInt("cap_amount");
//                boolean extra = rs.getBoolean("extra");
//                boolean Default = rs.getBoolean("default_env");
//
//                // Create envelope
//                Envelope e = new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, Default);
//                System.out.println("Envelope loaded: " + e.toString() + "\n");
////                EnvelopeAccess.addEnvelope(e); // Assuming EnvelopeAccess handles adding envelopes to your application
//            }
//        } catch (SQLException e) {
//            System.out.println("Failed to load envelopes from database: " + e.getMessage());
//        }
//    }
    
    /**
     * Creates a new SQLite database if it doesn't exist and initializes necessary tables.
     */
    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
                createTables(conn); // Ensure tables are created after database creation
            }
        } catch (SQLException e) {
            System.out.println("Failed to create database: " + e.getMessage());
        }
    }

    /**
     * Creates the 'envelopes' table if it doesn't exist.
     *
     * @param conn Connection to the SQLite database
     */
    public static void createTables(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS envelopes (\n"
                + " name TEXT PRIMARY KEY,\n"
                + " priority INTEGER NOT NULL,\n"
                + " amount TEXT NOT NULL,\n" // Change to TEXT
                + " fill_setting INTEGER,\n"
                + " fill_amount INTEGER,\n"
                + " cap BOOLEAN,\n"
                + " cap_amount INTEGER,\n"
                + " extra BOOLEAN,\n"
                + " default_env BOOLEAN\n"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tables have been created.");
        } catch (SQLException e) {
            System.out.println("Failed to create tables: " + e.getMessage());
        }
    }


    /**
     * Checks if a table with the given name exists in the database.
     *
     * @param tableName Name of the table to check
     * @return true if the table exists; false otherwise
     */
    public static boolean tableExists(String tableName) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, tableName, new String[] { "TABLE" })) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Failed to check table existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Adds a new envelope entry to the 'envelopes' table.
     *
     * @param name Name of the envelope
     * @param priority Priority of the envelope
     * @param amount Amount associated with the envelope
     * @param fillSetting Fill setting of the envelope
     * @param fillAmount Fill amount of the envelope
     * @param cap Cap flag of the envelope
     * @param capAmount Cap amount of the envelope
     * @param extra Extra flag of the envelope
     * @param defaultEnv Default envelope flag
     */
    public static void addEnvelope(String name, int priority, BigDecimal amount, int fillSetting, int fillAmount,
            boolean cap, int capAmount, boolean extra, boolean defaultEnv) {

        String sql = "INSERT INTO envelopes(name, priority, amount, fill_setting, fill_amount, cap, cap_amount, extra, default_env) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, priority);
            pstmt.setString(3, amount.toString()); // Store BigDecimal as String
            pstmt.setInt(4, fillSetting);
            pstmt.setInt(5, fillAmount);
            pstmt.setBoolean(6, cap);
            pstmt.setInt(7, capAmount);
            pstmt.setBoolean(8, extra);
            pstmt.setBoolean(9, defaultEnv);
            pstmt.executeUpdate();
            System.out.println("Envelope added successfully.");
        } catch (SQLException e) {
            System.out.println("Failed to add envelope: " + e.getMessage());
        }
    }

    public static void addEnvelope(Envelope e) {

        String sql = "INSERT INTO envelopes(name, priority, amount, fill_setting, fill_amount, cap, cap_amount, extra, default_env) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getName());
            pstmt.setInt(2, e.getPriority());
            pstmt.setString(3, e.getAmount().toString()); // Store BigDecimal as String
            pstmt.setInt(4, e.getFillSetting());
            pstmt.setInt(5, e.getFillAmount());
            pstmt.setBoolean(6, e.hasCap());
            pstmt.setInt(7, e.getCapAmount());
            pstmt.setBoolean(8, e.isExtra());
            pstmt.setBoolean(9, e.isDefault());
            pstmt.executeUpdate();
            System.out.println("Envelope added successfully.");
        } catch (SQLException f) {
            System.out.println("Failed to add envelope: " + f.getMessage());
        }
    }

    /**
     * Updates an existing envelope in the 'envelopes' table.
     *
     * @param name Name of the envelope to update
     * @param priority New priority value
     * @param amount New amount value
     * @param fillSetting New fill setting value
     * @param fillAmount New fill amount value
     * @param cap New cap flag value
     * @param capAmount New cap amount value
     * @param extra New extra flag value
     * @param defaultEnv New default envelope flag value
     * @return true if the envelope was updated successfully; false otherwise
     */
    public static boolean editEnvelope(String currentName, String newName, int priority, BigDecimal amount, int fillSetting, int fillAmount,
            boolean cap, int capAmount, boolean extra, boolean defaultEnv) {
        String sql = "UPDATE envelopes SET name = ?, priority = ?, amount = ?, fill_setting = ?, fill_amount = ?, cap = ?, cap_amount = ?, extra = ?, default_env = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, priority);
            pstmt.setString(3, amount.toString()); // Store BigDecimal as String
            pstmt.setInt(4, fillSetting);
            pstmt.setInt(5, fillAmount);
            pstmt.setBoolean(6, cap);
            pstmt.setInt(7, capAmount);
            pstmt.setBoolean(8, extra);
            pstmt.setBoolean(9, defaultEnv);
            pstmt.setString(10, currentName);
            pstmt.executeUpdate();
            System.out.println("Envelope updated successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update envelope: " + e.getMessage());
            return false;
        }
    }


    /**
     * Removes an envelope from the 'envelopes' table by name.
     *
     * @param name Name of the envelope to remove
     * @return true if the envelope was removed successfully; false otherwise
     */
    public static boolean removeEnvelope(String name) {
        String sql = "DELETE FROM envelopes WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Envelope removed successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to remove envelope: " + e.getMessage());
            return false;
        }
    }

    /**
     * Prints all envelopes currently stored in the 'envelopes' table.
     */
    public static void printAllEnvelopes() {
        String sql = "SELECT * FROM envelopes";

        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(rs.getString("name") + "\t" + rs.getInt("priority") + "\t"
                        + rs.getDouble("amount") + "\t" + rs.getInt("fill_setting") + "\t"
                        + rs.getInt("fill_amount") + "\t" + rs.getBoolean("cap") + "\t"
                        + rs.getInt("cap_amount") + "\t" + rs.getBoolean("extra") + "\t"
                        + rs.getBoolean("default_env"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to print envelopes: " + e.getMessage());
        }
    }
    
    /**
     * Returns the sum of the amount of all existing envelopes in the database.
     *
     * @return Total balance of all envelopes
     */
    public static BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO; // Default value or initial value

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(amount) AS total_amount FROM envelopes")) {

            if (rs.next()) {
                String totalAmountStr = rs.getString("total_amount");
                if (totalAmountStr != null) {
                    balance = new BigDecimal(totalAmountStr);
                }
            } else {
                // Handle case where no envelope data is found
                System.out.println("No envelopes found in the database.");
                // Alternatively, you could throw an exception or return a specific value
                // depending on your application's requirements
                // throw new IllegalStateException("No envelopes found");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
            // Handle SQLException appropriately (logging, exception propagation, etc.)
        }

        return balance;
    }
    
    public static Envelope getEnvelope(String name) {
        String sql = "SELECT * FROM envelopes WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int priority = rs.getInt("priority");
                    BigDecimal amount = new BigDecimal(rs.getString("amount")); // Retrieve as String and convert to BigDecimal
                    int fillSetting = rs.getInt("fill_setting");
                    int fillAmount = rs.getInt("fill_amount");
                    boolean cap = rs.getBoolean("cap");
                    int capAmount = rs.getInt("cap_amount");
                    boolean extra = rs.getBoolean("extra");
                    boolean defaultEnv = rs.getBoolean("default_env");

                    return new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, defaultEnv);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve envelope: " + e.getMessage());
        }
        return null;
    }

    public static List<Envelope> getEnvelopes() {
        List<Envelope> envelopes = new ArrayList<>();
        String sql = "SELECT * FROM envelopes";

        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int priority = rs.getInt("priority");
                String name = rs.getString("name");
                BigDecimal amount = new BigDecimal(rs.getString("amount")); // Retrieve as String and convert to BigDecimal
                int fillSetting = rs.getInt("fill_setting");
                int fillAmount = rs.getInt("fill_amount");
                boolean cap = rs.getBoolean("cap");
                int capAmount = rs.getInt("cap_amount");
                boolean extra = rs.getBoolean("extra");
                boolean defaultEnv = rs.getBoolean("default_env");

                envelopes.add(new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, defaultEnv));
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve envelopes: " + e.getMessage());
        }
        return envelopes;
    }


    public static Envelope getEnvelopeByPriority(int priority) {
        String sql = "SELECT * FROM envelopes WHERE priority = ?";
        
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, priority);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("name");
                BigDecimal amount = new BigDecimal(rs.getString("amount")); // Retrieve as String and convert to BigDecimal
                int fillSetting = rs.getInt("fill_setting");
                int fillAmount = rs.getInt("fill_amount");
                boolean cap = rs.getBoolean("cap");
                int capAmount = rs.getInt("cap_amount");
                boolean extra = rs.getBoolean("extra");
                boolean defaultEnv = rs.getBoolean("default_env");
                
                return new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, defaultEnv);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve envelope by priority: " + e.getMessage());
        }
        
        return null; // Return null if envelope with given priority is not found
    }
    
    public static Envelope getDefault() {
        String sql = "SELECT * FROM envelopes WHERE default_env = 1";
        
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int priority = rs.getInt("priority");
                String name = rs.getString("name");
                BigDecimal amount = new BigDecimal(rs.getString("amount")); // Retrieve as String and convert to BigDecimal
                int fillSetting = rs.getInt("fill_setting");
                int fillAmount = rs.getInt("fill_amount");
                boolean cap = rs.getBoolean("cap");
                int capAmount = rs.getInt("cap_amount");
                boolean extra = rs.getBoolean("extra");
                boolean defaultEnv = rs.getBoolean("default_env");
                
                return new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, defaultEnv);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve default envelope: " + e.getMessage());
        }
        
        return null; // Return null if default envelope is not found
    }
    
    public static Envelope getExtra() {
        String sql = "SELECT * FROM envelopes WHERE extra = 1";

        try (Connection conn = DriverManager.getConnection(URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
               
               if (rs.next()) {
                   int priority = rs.getInt("priority");
                   String name = rs.getString("name");
                   BigDecimal amount = new BigDecimal(rs.getString("amount")); // Retrieve as String and convert to BigDecimal
                   int fillSetting = rs.getInt("fill_setting");
                   int fillAmount = rs.getInt("fill_amount");
                   boolean cap = rs.getBoolean("cap");
                   int capAmount = rs.getInt("cap_amount");
                   boolean extra = rs.getBoolean("extra");
                   boolean defaultEnv = rs.getBoolean("default_env");
                   
                   return new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, defaultEnv);
               }
           } catch (SQLException e) {
               System.out.println("Failed to retrieve default envelope: " + e.getMessage());
           }
           
           return null; // Return null if default envelope is not found
    }
    
    public static boolean editAmount(String name, BigDecimal amount) {
        String sql = "UPDATE envelopes SET amount = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, amount.toString()); // Store BigDecimal as String
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Amount updated successfully for envelope: " + name);
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update amount for envelope " + name + ": " + e.getMessage());
            return false;
        }
    }
    

    public static boolean hasEnvelope(String name) {
        String sql = "SELECT COUNT(*) FROM envelopes WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters
            pstmt.setString(1, name);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // Check if there is at least one result
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Returns true if envelope with name exists, false otherwise
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // Default to false in case of errors or no results
    }

}
