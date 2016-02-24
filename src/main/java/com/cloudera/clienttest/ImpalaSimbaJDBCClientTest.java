package com.cloudera.clienttest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by ianbuss on 12/08/2014.
 */
public class ImpalaSimbaJDBCClientTest {

    private static String driverName = "com.cloudera.impala.jdbc41.Driver";

    public static void doTest(String hostName, String query, boolean secure) throws Exception {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        String secureConnectionSuffix = "";
        if (secure) {
            secureConnectionSuffix = ";AuthMech=1;KrbRealm=DEV;KrbHostFQDN=" + hostName + ";KrbServiceName=impala";
        } else {
            secureConnectionSuffix = ";AuthMech=0";
        }

        Connection con = null;
        try {
            String url = "jdbc:impala://" + hostName + ":21050/" + secureConnectionSuffix;
            con = DriverManager.getConnection(url);
            Statement stmt = con.createStatement();
            System.out.println("Executing: " + query);
            stmt.setFetchSize(1000);
            stmt.setQueryTimeout(10);
            ResultSet res = stmt.executeQuery(query);
            int cols = res.getMetaData().getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= cols; i++) {
                    System.out.print(String.valueOf(res.getString(i)) + "\t");
                }
                System.out.println();
            }
            stmt.close();
            System.out.println("Query finished");
        } finally {
            if (null != con) {
                con.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String hostName = "";
        String query = "";
        boolean secure = false;
        if (args.length > 1) {
            hostName = args[0];
            query = args[1];
        }
        else {
            System.err.println("Usage: " + ImpalaSimbaJDBCClientTest.class.getSimpleName() + " <host> <query> [secure (true|false)]");
            System.exit(1);
        }
        if (args.length > 2) {
            secure = Boolean.parseBoolean(args[2]);
        }

        ImpalaSimbaJDBCClientTest.doTest(hostName, query, secure);
    }

}
