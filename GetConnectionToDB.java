package itea.web07;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnectionToDB {

	private Connection connection = null;
	
	public GetConnectionToDB(String linkToDB) {
		
		try {
			System.out.print("Loading driver to connect to database...");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.print(" Successfully\n");
		} catch (Exception ex) {
			System.out.print("UNSUCCESSFULLY!!!\n The program will be terminated.\n");
			System.exit(0);
		}

		System.out.print("Connecting to the database...");

		if (linkToDB != null) {
			try {
				connection = DriverManager.getConnection(linkToDB);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(
					"UNSUCCESSFULLY!!!\nThe link to database is null. Impossible to establish connections. The program will be terminated. ");
			System.exit(0);
		}

		System.out.print(" Succesfully\n\n");
	}
	
	public Connection getConnectionToDB() {
		return connection;
	}
}
