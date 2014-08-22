package com.cloudera.clienttest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by ianbuss on 12/08/2014.
 */
public class HiveJDBCClientTest {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void doTest(String hostName, String principal, boolean secure) throws Exception {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        String secureConnectionSuffix = "";
        if (secure && null != principal && !principal.isEmpty()) {
            secureConnectionSuffix = ";principal="+principal;
        }

        String url = "jdbc:hive2://" + hostName + ":10000/default" + secureConnectionSuffix;
        Connection con = DriverManager.getConnection(url, principal, "");
        Statement stmt = con.createStatement();
        String tableName = "testHiveDriverTable";
        stmt.execute("drop table if exists " + tableName);
        stmt.execute("create table " + tableName + " (key int, value string)");
        // show tables
        String sql = "show tables '" + tableName + "'";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        if (res.next()) {
            System.out.println(res.getString(1));
        }

        // describe table
        sql = "describe " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
        }

        sql = "INSERT INTO TABLE " + tableName + " SELECT stack(2, 1, \"row1\", 2, \"row2\")" +
                " AS (column1, column2) FROM sample_07 limit 2";
        System.out.println("Running: " + sql);
        stmt.execute(sql);

        // select * query
        sql = "select * from " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
        }

        // regular hive query
        sql = "select count(1) from " + tableName;
        System.out.println("Running: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }

        // Cleanup
        stmt.execute("drop table " + tableName);
    }

    public static void main(String[] args) throws Exception {
        String hostName = "";
        String principal = "";
        boolean secure = false;
        if (args.length > 1) {
            hostName = args[0];
            principal = args[1];
        }
        else {
            System.err.println("Usage: " + HiveJDBCClientTest.class.getSimpleName() + " <host> <user> [secure (true|false)]");
            System.exit(1);
        }
        if (args.length > 2) {
            secure = Boolean.parseBoolean(args[2]);
        }

        HiveJDBCClientTest.doTest(hostName, principal, secure);
    }

}
