package itea.web07;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DBOperator {

	private Connection connection = null;
	private ResultSet resultSet = null;
	private PreparedStatement statement = null;
	private String userName;

	private final String LINKTODB = "jdbc:mysql://localhost/?user=root&password=";
	public static final String SELECT = "SELECT login, name FROM credentials WHERE login = ? AND password = ?";

	public DBOperator() {

		GetConnectionToDB getConnection = new GetConnectionToDB(LINKTODB);
		connection = getConnection.getConnectionToDB();

		if (connection != null) {
			try {
				statement = connection.prepareStatement(SELECT);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("The connection is null. Is your database server online?");
		}
	}

	public void saveUserToDB(String login, String password, String name, String region, String gender, String comment)
			throws SQLException {

		statement.execute("CREATE DATABASE IF NOT EXISTS web;");
		statement.execute("USE web;");
		statement.execute("CREATE TABLE IF NOT EXISTS credentials (id INT PRIMARY KEY AUTO_INCREMENT, "
				+ "login VARCHAR(255), password VARCHAR(512), name VARCHAR(255), region VARCHAR(40), gender VARCHAR(1), comment VARCHAR(255)); ");

		statement.execute("INSERT INTO credentials (login, password, name,region, gender,comment ) VALUES ('" + login
				+ "','" + password + "','" + name + "','" + region + "','" + gender + "','" + comment + "')");

		closeResources();
	}

	public boolean checkUserCredentials(String login, String password) throws SQLException {

		statement = connection.prepareStatement(SELECT);

		statement.setString(1, login);
		statement.setString(2, Encoding.sha256Encoding(password));
		statement.execute("USE web");

		resultSet = statement.executeQuery();
		String loginFromDB;

		boolean isCredentialsFound = false;

		while (resultSet.next()) {

			loginFromDB = resultSet.getString("login");
			userName = resultSet.getString("name");

			if (Objects.equals(loginFromDB, login)) {
				isCredentialsFound = true;
				break;
			}
		}

		closeResources();

		return isCredentialsFound;

	}

	private void closeResources() {
		try {
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {

		return userName;
	}
}
