package CPS_610.lab01;

import java.io.*;
import java.sql.*;

public class CalGPA {

    public static void main(String args[]) throws SQLException, IOException {
        try {
            Class.forName("oracle.jdbc.driver.OrableDriver");
        } catch (ClassNotFoundException x) {
            System.out.println("Driver could not be loaded.");
        }

        String dbacct, passwrd, name;
        char grade;
        int credit;

        dbacct = readEntry("Enter database account: ");
        passwrd = readEntry("Enter password: ");

        Connection conn = DriverManager.getConnection("jdbc:oracle:oci8:" + dbacct + "/" + passwrd);

        String stmt1 = "SELECT G.Grade, C.Credit_hours " +
                "FROM STUDENT S, GRADE_REPORT G, SECTION SEC, COURSE C " +
                "WHERE G.Student_number=S.Student_number AND " +
                "G.Section_identifier=SEC.Section_identifier AND " +
                "SEC.Course_number=C.Course_number AND S.Name=?";

        PreparedStatement p = conn.prepareStatement(stmt1);
        name = readEntry("Please enter your name: ");
        p.clearParameters();
        p.setString(1, name);

        ResultSet r = p.executeQuery();

        double count = 0, sum = 0, avg = 0;

        while (r.next()) {
            grade = r.getString(1).charAt(0);
            credit = r.getInt(2);

            switch (grade) {
                case 'A':
                    sum += (4 * credit);
                    count++;
                    break;
                case 'B':
                    sum += (3 * credit);
                    count++;
                    break;
                case 'C':
                    sum += (2 * credit);
                    count++;
                    break;
                case 'D':
                    sum += (1 * credit);
                    count++;
                    break;
                case 'F':
                    sum += (0 * credit);
                    count++;
                    break;
                default:
                    System.out.println("This grade " + grade + " will not be calculated.");
                    break;
            }
        }

        avg = sum / count;
        System.out.println("Student named " + name + " has a grade point average " + avg + ".");
        r.close();
    }

    // Additional method to read input
    private static String readEntry(String prompt) throws IOException {
        System.out.print(prompt);
        System.out.flush();
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }
}
