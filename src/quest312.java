import java.sql.*;

/**
 * Question 3.12 involves insert, update, and delete statements for the
 * university database.
 *
 * @author Andrew McDaniels and April Crawford
 * @version Question 3.12, Assignment 4, 04/11/17
 */
public class quest312 {
    Connection conn;
    Statement stmt;
    ResultSet rset;

    public quest312(Connection c1, Statement s1, ResultSet rs1) {
        conn = c1;
        stmt = s1;
        rset = rs1;
    }

    /**
     * Create a new course "CS-001", titled "Weekly Seminar", with 1 credit.  |
     * (dept_name not specified, set to null)
     *
     * insert into course
     * 		values("CS-001", "Weekly Seminar", null, 1)
     *
     * @throws SQLException
     */
    public void run312a() throws SQLException {
        PreparedStatement pstmt =
                conn.prepareStatement(
                        "Insert into course values (?, ?, ?, ?)");
        pstmt.setString(1, "CS-001");
        pstmt.setString(2, "Weekly Seminar");
        pstmt.setString(3, null);
        pstmt.setInt(4, 1);
        pstmt.execute();
        pstmt.close();

        //print the new tuple
        rset = stmt.executeQuery(
                "SELECT course_id from course");
        System.out.println("Create a new course \"CS-001\", titled"
                + " \"Weekly Seminar\", with 1 credit.");
        System.out.println("New course:");
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }//end while
    }//end run3123a

    /**
     * Create a section of this course in Fall 2009, with sec_id of 1
     * (building, room_number, time_slot_id not specified, set to null)
     *
     * insert into section
     * 		values("CS-001", "1", "Fall", 2009, null, null, null)
     * @throws SQLException
     */
    public void run312b() throws SQLException{
        PreparedStatement pstmt =
                conn.prepareStatement(
                        "Insert into section values (?, ?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, "CS-001");
        pstmt.setString(2, "1");
        pstmt.setString(3, "Fall");
        pstmt.setInt(4, 2009);
        pstmt.setString(5, null);
        pstmt.setString(6, null);
        pstmt.setString(7, null);
        pstmt.execute();
        pstmt.close();

        //print the new tuple
        rset = stmt.executeQuery(
                "SELECT * from section WHERE course_id = 'CS-001' AND "
                        + "sec_id = '1' AND semester = 'Fall' AND year = 2009");
        System.out.println("Create a section of this course in Fall 2009, with"
                + " sec_id of 1.");
        System.out.println("New section:");
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }//end while
    }//end run312b

    /**
     * Enroll every student in the Comp. Sci. department in the above section.
     *
     * insert into takes
     * 		select id
     * 		from student
     * 		where dept_name = "Comp. Sci."
     *
     * update student
     * set credits = credits + 1
     * where dept_name = "Comp. Sci."
     * @throws SQLException
     */
    public void run312c() throws SQLException{
        PreparedStatement ps = conn.prepareStatement(
                "Insert into student values (?, ?, ?, ?)");
        ps.setString(1, "66666");
        ps.setString(2, "Chavez");
        ps.setString(3, "Comp. Sci.");
        ps.setInt(4, 7);
        ps.execute();


        //enroll all CS students in CS-001
        rset = stmt.executeQuery(
                "SELECT id FROM student WHERE dept_name = 'Comp. Sci.'");
        PreparedStatement preparedStatement = conn.prepareStatement(
                "Insert into takes values (?, ?, ?, ?, ?, ?)");
        while(rset.next()){
            preparedStatement.setString(1, rset.getString(1));
            preparedStatement.setString(2, "CS-001");
            preparedStatement.setString(3, "1");
            preparedStatement.setString(4, "Fall");
            preparedStatement.setInt(5, 2009);
            preparedStatement.setString(6, null);
            preparedStatement.execute();
        }

        rset = stmt.executeQuery(
                "SELECT DISTINCT name FROM student NATURAL JOIN takes " +
                        "WHERE takes.course_id LIKE \'CS%\'");

        System.out.println("Printing off names after updates");
        System.out.println("Names");
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }
        //update students for extra class enrolled in
        //all are enrolled in a new course, all are +1
        stmt = conn.createStatement();
        String sql = "UPDATE student " +
                "SET tot_cred = tot_cred + 1 "
                + "WHERE dept_name = 'Comp. Sci.'";
        stmt.executeUpdate(sql);

        //show updated credits
        rset = stmt.executeQuery(
                "SELECT name, tot_cred FROM student "
                        + "WHERE dept_name = 'Comp. Sci.'");
        System.out.println("All Comp. Sci. students with updated credits:");
        System.out.println("name \t tot_cred");
        while (rset.next()) {
            System.out.print(rset.getString(1) +"\t");
            System.out.println(rset.getDouble(2));
        }//end while
    }//end run312c


    /**
     * Delete enrollments in the above section where the student's name is
     * Chavez
     *
     * delete from takes
     * where ID in (select ID
     * 				from student
     * 				where name = "Chavez")
     *
     * @throws SQLException
     */
    public void run312d() throws SQLException{

        String sql2 = "DELETE FROM takes WHERE id IN (SELECT id FROM student"
                + " WHERE name = 'Chavez')";
        stmt.executeUpdate(sql2);

        //print the new tuple
        rset = stmt.executeQuery(
                "SELECT * FROM student WHERE id IN (SELECT id FROM takes WHERE "
                        + "course_id = 'CS-001' AND semester = 'Fall' AND year = 2009)");
        System.out.println("Show students taking CS-001, none of which should"
                + " be named Chavez.");
        System.out.println("Printing off all the student.");
        System.out.println("Students:");
        while (rset.next()) {
            System.out.println(rset.getString(2));
        }//end while
    }//end run 312d

    /**
     * Delete the course CS-001.  What will happen if you run this delete
     * statement without first deleting offerings (sections) from this course?
     * Foreign key constraints --> In section DDL..
     * foreign key (course_id) references course
     * 		on delete cascade
     * @throws SQLException
     */
    public void run312e() throws SQLException{
        //delete from section
        String sql = "DELETE FROM section WHERE course_id = 'CS-001' "
                + "AND semester = 'Fall' AND year = 2009";
        stmt.executeUpdate(sql);

        //print the new tuple
        rset = stmt.executeQuery(
                "SELECT * FROM section");
        ResultSetMetaData rsmd = rset.getMetaData();
        rsmd.getColumnName(1);
        System.out.println("Show all sections, none of which should be the new"
                + "section.");
        System.out.println(rsmd.getColumnName(1));
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }//end while



        //delete from course
        String sql2 = "DELETE FROM course WHERE course_id = 'CS-001'";
        stmt.executeUpdate(sql2);

        //print the new tuple
        rset = stmt.executeQuery(
                "SELECT course_id FROM course");
        System.out.println("Show all courses, none of which should be the new"
                + "course.");
        System.out.println("Courses:");
        while (rset.next()) {
            System.out.println(rset.getString(1));
        }//end while
    }//end run312e

    /**
     * Delete all takes types corresponding to any section of any course with
     * the word "database" as part of the title; ignore case when matching the
     * word with the title.
     * @throws SQLException
     */
    public void run312f() throws SQLException{
        rset = stmt.executeQuery("SELECT title FROM course NATURAL JOIN takes");
        System.out.println("Printing off all courses that have takes, should have database in title");
        while(rset.next()){
            System.out.println(rset.getString(1));
        }
        String sql = "DELETE FROM takes WHERE course_id IN( SELECT course_id FROM course WHERE LOWER(title) LIKE" +
                " '%database%')";
        stmt.executeUpdate(sql);

        rset = stmt.executeQuery("SELECT title FROM course NATURAL JOIN takes");
        System.out.println("Printing off all courses that have takes, should NOT have database in title");
        while(rset.next()){
            System.out.println(rset.getString(1));
        }

    }//end run312f
}
