package org.example;

import java.sql.*;
public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "postgres";
        String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make a connection!");
            }

            try (Statement myStmt = conn.createStatement();
                 ResultSet myRs = myStmt.executeQuery("SELECT * FROM reiziger")) {

                System.out.println("Alle reizigers:");
                while (myRs.next()) {
                    int reizigerId = myRs.getInt("reiziger_id");
                    String voorletters = myRs.getString("voorletters");
                    String tussenvoegsel = myRs.getString("tussenvoegsel") != null ? myRs.getString("tussenvoegsel") + " " : "";
                    String achternaam = myRs.getString("achternaam");
                    String geboortedatum = myRs.getDate("geboortedatum").toString();

                    String line = "    #" + reizigerId + ": " + voorletters + " " + tussenvoegsel + achternaam + " (" + geboortedatum + ")";
                    System.out.println(line);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database fout: " + e.getMessage());
        }
    }
}
