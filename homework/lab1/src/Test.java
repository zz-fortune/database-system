import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;


public class Test {
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		System.out.println(pattern.matcher("4").matches());
		
	}
}
