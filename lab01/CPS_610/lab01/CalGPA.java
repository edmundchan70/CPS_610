package CPS_610.lab01;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class CalGPA {

	public static void main(String args[]) throws SQLException, IOException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException x) {
			System.out.println("Driver could not be loaded.");
		}
		String  name; char grade; int credit;
		Connection conn = connectToDB();
		String stmt1 = "select g.grade, c.credit_hours \r\n"
				+ "FROM grade_report g join student s \r\n"
				+ "on g.student_number = s.student_number\r\n"
				+ "join section sec on g.section_identifier = sec.section_identifier\r\n"
				+ "join course c on c.course_number = sec.course_number\r\n"
				+ "where s.name=?";
		PreparedStatement p = conn.prepareStatement(stmt1);
				name = readEntry("Please enter your name: ");
				p.clearParameters();
				p.setString(1, name);
				ResultSet r = p.executeQuery();
				double count=0, sum=0, avg=0;
				while(r.next()){
					
					grade = r.getString("grade").charAt(0);
					credit = r.getInt(2);
					switch (grade){
						case 'A': sum=sum+(4*credit); count=count+1; break;
						case 'B': sum=sum+(3*credit); count=count+1; break;
						case 'C': sum=sum+(2*credit); count=count+1; break;
						case 'D': sum=sum+(1*credit); count=count+1; break;
						case 'F': sum=sum+(0*credit); count=count+1; break;
						default: System.out.println("This grade "+grade+" will not be calculated.");
				
				}
				};
				avg = sum/count;
				System.out.println("Student named "+name+" has a grade point average "+avg+".");
				r.close();
		System.out.println("FINISHED");
	}

	private static String readEntry(String question) {
		Scanner scan = new Scanner(System.in);
		System.out.println(question);

		return scan.nextLine();
	}

	private static Connection connectToDB() {
		String user = "sys as sysdba";
		String password = "233233123Lol";
		String url = "jdbc:oracle:thin:@//localhost:1521/xe";
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		props.setProperty("internal_logon", "SYSDBA");
		try {
			return DriverManager.getConnection(url, props);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

/**
 * select g.grade, c.credit_hours 
FROM grade_report g join student s 
on g.student_number = s.student_number
join section sec on g.section_identifier = sec.section_identifier
join course c on c.course_number = sec.course_number
where s.name='Mike Johnson';
 * 
 * select g.grade FROM grade_report g join student s on g.student_number =
 * s.student_number where s.name='Mike Johnson';
 */
