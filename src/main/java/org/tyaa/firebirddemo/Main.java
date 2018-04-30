/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.firebirddemo;

/**
 *
 * @author yurii
 */
public class Main {

    public static void main(String[] args) {

        String databaseURL = "jdbc:firebirdsql:localhost/3050:/home/yurii/Загрузки/Градиент/db/teploset_new.fdb?sql_dialect=3";
        String user = "SYSDBA";
        String password = "1";
        String driverName = "org.firebirdsql.jdbc.FBDriver";

        java.sql.Driver d = null;
        java.sql.Connection c = null;
        java.sql.Statement s = null;
        java.sql.ResultSet rs = null;

        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
        } catch (java.lang.ClassNotFoundException e) {
            // A call to Class.forName() forces us to consider this exception :-)...
            System.out.println("Firebird JCA-JDBC driver not found in class path");
            System.out.println(e.getMessage());
            return;
        }

        //System.out.println("reg - ok");
        try {
            // We pass the entire database URL, but we could just pass "jdbc:interbase:"
            d = java.sql.DriverManager.getDriver(databaseURL);
            System.out.println("Firebird JCA-JDBC driver version "
                    + d.getMajorVersion()
                    + "."
                    + d.getMinorVersion()
                    + " registered with driver manager.");
        } catch (java.sql.SQLException e) {
            System.out.println("Unable to find Firebird JCA-JDBC driver among the registered drivers.");
            showSQLException(e);
            return;
        }

        try {
            c = java.sql.DriverManager.getConnection(databaseURL, user, password);
            System.out.println("Connection established.");
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to establish a connection through the driver manager.");
            showSQLException(e);
            return;
        }

        try {
            java.sql.DatabaseMetaData dbMetaData = c.getMetaData();

            // Ok, let's query a driver/database capability
            if (dbMetaData.supportsTransactions()) {
                System.out.println("Transactions are supported.");
            } else {
                System.out.println("Transactions are not supported.");
            }

            // What are the views defined on this database?
            java.sql.ResultSet tables = dbMetaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME") + " is a view.");
            }
            tables.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Unable to extract database meta data.");
            showSQLException(e);
            // What the heck, who needs meta data anyway ;-(, let's continue on...
        }

    }

    private static void showSQLException(java.sql.SQLException e) {
        // Notice that a SQLException is actually a chain of SQLExceptions,
        // let's not forget to print all of them...
        java.sql.SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error Code: " + next.getErrorCode());
            System.out.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }
}
