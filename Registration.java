package itea.web07;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Registration extends HttpServlet {

	private static final long serialVersionUID = -8843749250963557837L;

	private String login;
	private String password;
	private String rePassword;
	private String fullName;
	private String region;
	private String gender;
	private String comment;
	private String agreement;
	private boolean isError;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
		session.getAttribute(Login.USER_ACCESS_GRANTED);

		if (session.getAttribute(Login.USER_ACCESS_GRANTED) == null) {
			getDataFromRequest(request);

			createForm(out, "");

		} else {
			out.write(Menu.MENU_START + Menu.MENU_LOGIN + Menu.MENU_SECRET + Menu.MENU_END);
			out.write("You have already authorized");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		getDataFromRequest(request);

		String errors = checkData();

		if (isError) {
			createForm(out, errors);
		} else {
			out.write(Menu.MENU_START + Menu.MENU_LOGIN + Menu.MENU_SECRET + Menu.MENU_END);
			out.write("Registration successful. You should use your new credentials to login.");
			
			try {
				DBOperator dbOperator = new DBOperator();
				dbOperator.saveUserToDB(login, Encoding.sha256Encoding(password), fullName, region, gender, comment);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void createForm(PrintWriter out, String errors) {

		String registrationForm = String.format("<center>\r\n" + "		   <form action='' method = 'post'>\r\n"
				+ "			   <table border='0'>\r\n" + "				  <tr>\r\n"
				+ "					 <td width='100'>Login</td>\r\n"
				+ "					 <td ><input type='email' name='login' value='%s' /></td>\r\n"
				+ "				  </tr>\r\n" + "				  <tr>\r\n" + "					 <td>Password</td>\r\n"
				+ "					 <td><input type='password' name='password' /></td>\r\n"
				+ "				  </tr>\r\n" + "				  <tr>\r\n"
				+ "					 <td>Re-Password</td>\r\n"
				+ "					 <td><input type='password' name='rePassword' /></td>\r\n"
				+ "				  </tr>\r\n" + "				  <tr>\r\n" + "					 <td>Full name</td>\r\n"
				+ "					 <td><input type='text' name='fullName' value='%s' /></td>\r\n"
				+ "				  </tr>\r\n" + "				  <tr>\r\n" + "					 <td>Region</td>\r\n"
				+ "					 <td>\r\n" + "						 <select name = 'region'>\r\n"
				+ "						 <option value = 'Lviv' %s>Lviv region</option>\r\n"
				+ "						 <option value = 'Kyiv' %s>Kyiv region</option>\r\n"
				+ "						 <option value = 'Kharkiv' %s>Kharkiv region</option>\r\n"
				+ "						 </select>\r\n" + "					 </td>\r\n" + "				  </tr>\r\n"
				+ "				  <tr>\r\n" + "					 <td>Gender</td>\r\n"
				+ "						<td>\r\n"
				+ "							F <input type='radio' value = 'F' name='gender' %s/>\r\n"
				+ "							M <input type=\"radio\" value = 'M' name='gender' %s/>\r\n"
				+ "						</td>\r\n" + "				  </tr>\r\n" + "				  <tr>\r\n"
				+ "					 <td>Comment</td>\r\n" + "						<td>\r\n"
				+ "							<textarea cols = '10' rows = '5' name = 'comment'>%s</textarea>\r\n"
				+ "						</td>\r\n" + "						\r\n" + "						\r\n" + "%s"

				+ "				  </tr>\r\n" + "				  <tr>\r\n"
				+ "					 <td>I agree to terms</td>\r\n" + "						<td>\r\n"
				+ "							<input type='checkbox' name='agreement'/>\r\n"
				+ "						</td>\r\n" + "				  </tr>\r\n" + "				  <tr>\r\n"
				+ "					 <td align='center' colspan='2'><input type='submit' value='Send' /></td>\r\n"
				+ "				  </tr>\r\n" + "			   </table>\r\n" + "		   </form>\r\n"
				+ "	  </center>", login != null ? login : "", fullName != null ? fullName : "",
				"Lviv".equals(region) ? "selected" : "", "Kyiv".equals(region) ? "selected" : "",
				"Kharkiv".equals(region) ? "selected" : "", "F".equals(gender) ? "checked" : "",
				"M".equals(gender) ? "checked" : "", comment != null ? comment : "",
				isError ? ("<td width='300'>" + errors + "</td>") : "");

		out.write(Menu.MENU_START + Menu.MENU_LOGIN + Menu.MENU_SECRET + Menu.MENU_END);
		out.write(registrationForm);
	}

	private String checkData() {

		StringBuilder errorText = new StringBuilder("<ul>");
		isError = false;
		if (login == null || login.isEmpty()) {
			isError = true;
			errorText.append("<li>Login is empty</li>");
		} else {
			if (!Pattern.matches("^(.+)@(.+)$", login)) {
				isError = true;
				errorText.append("<li>Incorrect login: must be valid Email</li>");
			}
		}

		if (password == null || password.isEmpty()) {
			isError = true;
			errorText.append("<li>Password is empty</li>");
		} else {
			if (!Pattern.matches("(?=.*[A-Z])(?=.*[a-z])(?=(.*\\d){2})[A-Za-z\\d]{8,}", password)) {
				isError = true;
				errorText.append(
						"<li>Password is too weak. It must contain letters, capital letters, minimum 2 digits, minimum length 8</li>");
			}
		}

		if (rePassword == null || rePassword.isEmpty()) {
			isError = true;
			errorText.append("<li>Retyped password is empty</li>");
		} else {
			if (!rePassword.equals(password)) {
				isError = true;
				errorText.append("<li>Password and retyped password aren't the same</li>");
			}
		}

		if (fullName == null || fullName.isEmpty()) {
			isError = true;
			errorText.append("<li>Name is empty</li>");
		}

		if (region == null || region.isEmpty()) {
			isError = true;
			errorText.append("<li>Region is empty</li>");
		} else {

			if (!("Lviv".equals(region) || "Kyiv".equals(region) || "Kharkiv".equals(region))) {
				isError = true;
				errorText.append("<li>Choose correct region</li>");
			}
		}

		if (gender == null || gender.isEmpty()) {
			isError = true;
			errorText.append("<li>Choose gender</li>");
		} else {
			if (!("M".equals(gender) || "F".equals(gender))) {
				isError = true;
				errorText.append("<li>Choose correct gender</li>");
			}
		}

		if (agreement == null || agreement.isEmpty()) {
			isError = true;
			errorText.append("<li>You must check agree checkbox</li>");
		}

		errorText.append("</ul>");
		errorText.trimToSize();

		return errorText.toString();
	}

	private void getDataFromRequest(HttpServletRequest request) {
		isError = false;
		login = request.getParameter("login");
		password = request.getParameter("password");
		rePassword = request.getParameter("rePassword");
		fullName = request.getParameter("fullName");
		region = request.getParameter("region");
		gender = request.getParameter("gender");
		comment = request.getParameter("comment");
		agreement = request.getParameter("agreement");
	}
}
