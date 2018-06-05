import java.io.Console;
import java.sql.*;
import java.util.Scanner;

/**
 * Main class to run classes quest311 and quest312 containing answers to
 * problems 3.11 and 3.12.
 *
 * @author Andrew McDaniels and April Crawford
 * @version Assignment 4, 04/11/17
 */
public class main {
    public static void main(String[] args) {

        try {
            Class.forName(
                    "org.postgresql.Driver").newInstance();
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
            System.exit(0);
        }//end try/catch


        Scanner scan = new Scanner(System.in);

        System.out.print("Please enter username: \t");
        String user = scan.nextLine();
        System.out.print("Please enter your password for " + user + "\t" );
        Console cons = System.console();
        char[] password = cons.readPassword();
        String pass = new String(password);
        System.out.print("Please enter database name: \t");
        String database = scan.nextLine();
        String hostname = "localhost";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        String initStat = "jdbc:postgresql://" +
                hostname + "/" + database + "?user=" + user
                + "&password=" + pass;

        //RUN METHODS FOR PROBLEM 3.11
        try{
            conn = DriverManager.getConnection(initStat);
            stmt = conn.createStatement();

            quest311 first = new quest311(conn, stmt, rset);
            first.run311a();
            first.run311b();
            first.run311c();
            first.run311d();
        } catch (SQLException e) {
            System.out.println("Exception: " + e.toString());
            System.exit(0);
        }//end try/catch

        //RUN METHODS FOR PROBLEM 3.12
        try{
            conn = DriverManager.getConnection(initStat);
            stmt = conn.createStatement();

            quest312 first = new quest312(conn, stmt, rset);
            first.run312a();
            first.run312b();
            first.run312c();
            first.run312d();
            first.run312e();
            first.run312f();
        } catch (SQLException e) {
            System.out.println("Exception: " + e.toString());
            System.exit(0);
        }//end try/catch
    }//end main method
}//end main
