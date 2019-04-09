import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Test {
	public static void main(String[] args) {
		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	    final String DB_URL = "jdbc:mysql://localhost:3306/STU_MANAGEMENT";
	    
	    final String user = "root";
	    final String password = "zz1160300620";
	    
	    Connection connection = null;
	    Statement statement = null;
	    
	    try {
			Class.forName(JDBC_DRIVER);
			
			System.out.println("连接数据库");
			connection = DriverManager.getConnection(DB_URL, user, password);
			
			System.out.println(" 实例化Statement对象...");
			statement = connection.createStatement();
			
			String sql = "SELECT * FROM student";
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				System.out.println(rs.getString("Sid"));
			}
			
			rs.close();
			statement.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
