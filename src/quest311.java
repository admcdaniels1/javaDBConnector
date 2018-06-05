import java.sql.*;

/**
 * Question 3.11 involves select statements for the university database.
 *
 * @author Andrew McDaniels and April Crawford
 * @version Question 3.11, Assignment 4, 04/11/17
 */
public class quest311 {
    //connection for the database
    Connection conn;
    //statements for queries
    Statement stmt;
    //result sets for returning values
    ResultSet rset;

    /**
     * Constructor to initialize connection, statement, and result set variables.
     * @param c1 Connection
     * @param s1 Statement
     * @param rs1 ResultSet
     */
    public quest311(Connection c1, Statement s1, ResultSet rs1) {
        conn = c1;
        stmt = s1;
        rset = rs1;
    }//end constructor

    /**
     * Find the names of all the students who have taken at least one Comp. Sci.
     * course; make sure there are no duplicate names in the result.
     *
     * select distinct name
     * from student natural join takes
     * where takes.course_id like \"CS%\"
     *
     * @throws SQLException
     */
    public void run311a() throws SQLException {
        rset = stmt.executeQuery(
                "SELECT DISTINCT name FROM student NATURAL JOIN takes " +
                        "WHERE takes.course_id LIKE \'CS%\'");
        System.out.println("Find the names of all students who have taken at"
                + " least one Comp. Sci. course; make sure there are no "
                + "duplicate names in the result");
        System.out.println("Names");
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }//end while
    }//end run311a

    /**
     * *NEED TO CORRELATE THIS?*
     * Find the IDs and names of all the students who have not taken any
     * course offering before Spring 2009.
     *
     * SELECT s.id, name
     * FROM student as s
     * WHERE s.id NOT EXIST (SELECT student.id
     * 						 FROM student NATURAL JOIN takes
     * 						 WHERE takes.year < \"2009\")
     *
     * @throws SQLException
     */
    public void run311b() throws SQLException{
        rset = stmt.executeQuery("SELECT s.id, name FROM student as s "
                + "WHERE s.id NOT IN (SELECT student.id FROM student "
                + "NATURAL JOIN takes WHERE takes.year < \'2009\')");
        System.out.println("Find the IDs and names of all students who have not"
                + " taken any course offering before Spring 2009.");
        System.out.println("id \t name");
        while(rset.next()){
            System.out.print(rset.getString(1) + "\t");
            System.out.println(rset.getString(2));
        }//end while
    }//end run311b

    /**
     * For each department, find the maximum salary of instructors in that
     * department.  You may assume that every department has at least one
     * instructor.
     *
     * SELECT dept_name, MAX(salary)
     * FROM instructor
     * GROUP BY dept_name
     *
     * @throws SQLException
     */
    public void run311c() throws SQLException{
        rset = stmt.executeQuery("SELECT dept_name, MAX(salary) FROM instructor" +
                " GROUP BY dept_name");
        System.out.println("For each department, find the maximum salary of "
                + "instructors in that department. You mas assume that every "
                + "department has at least one instructor.");
        System.out.println("dept_name \t MAX(salary)");
        while(rset.next()){
            System.out.print(rset.getString(1) + "\t");
            System.out.println(rset.getDouble(2));
        }//end while
    }//end run311c

    /**
     * Find the lowest, across all departments, of the per-department maximum
     * salary computed by the preceding query.
     *
     * WITH prevQ(dept_name, maxxer) AS
     * 		SELECT dept_name, MAX(salary)
     * 		FROM instructor
     * 		GROUP BY dept_name)
     * SELECT MIN(maxxer)
     * FROM preQ
     *
     * @throws SQLException
     */
    public void run311d() throws SQLException{
        rset = stmt.executeQuery("WITH prevQ(dept_name, maxxer) AS " +
                "(SELECT dept_name, MAX(salary) FROM instructor" +
                " GROUP BY dept_name) SELECT MIN(maxxer) FROM prevQ");
        System.out.println("Find the lowest across all departments, of the "
                + "per-department maximum salary computed by the preceding "
                + "query.");
        System.out.println("salary");
        while(rset.next()){
            System.out.println(rset.getDouble(1));
        }//end while
    }//end run311d

}//end quest311
