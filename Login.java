package itea.web07;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {

	DBOperator dbOperator;

	private static final long serialVersionUID = -9071757192018689176L;
	public static final String USER_ACCESS_GRANTED = "access";

	String formLogin = "<html><center>\r\n" + "		   <form action=\"\" method='post'>\r\n"
			+ "		   <table border=\"0\">\r\n" + "		      <tr>\r\n"
			+ "		         <td width=\"100\">Login</td>\r\n"
			+ "		         <td ><input type=\"text\" name=\"login\" /></td>\r\n" + "		      </tr>\r\n"
			+ "		      <tr>\r\n" + "		         <td>Password</td>\r\n"
			+ "		         <td><input type=\"text\" name=\"password\" /></td>\r\n" + "		      </tr>\r\n"
			+ "		      <tr>\r\n"
			+ "		         <td align=\"center\" colspan=\"2\"><input type=\"submit\" value=\"Send\" /></td>\r\n"
			+ "		      </tr>\r\n" + "\r\n" + "		   </table>\r\n" + "		   </form></center></html>";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		session.getAttribute(USER_ACCESS_GRANTED);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String key = request.getParameter("key");

		if (key != null) {
			session.invalidate();

		}
		session = request.getSession(true);
		if (session.getAttribute(USER_ACCESS_GRANTED) == null) {

			out.write(Menu.MENU_START + Menu.MENU_REGISTRATION + Menu.MENU_SECRET + Menu.MENU_END);
			out.write(formLogin);

		} else {
			out.write(Menu.MENU_START + Menu.MENU_LOGOUT + Menu.MENU_SECRET + Menu.MENU_END);
			out.write("Hello " + dbOperator.getUserName() + ", you are successfully authorized");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		dbOperator = new DBOperator();

		if (login != null) {

			try {
				if (dbOperator.checkUserCredentials(login, password)) {

					HttpSession session = request.getSession();
					session.setAttribute(USER_ACCESS_GRANTED, true);
					out.write(Menu.MENU_START + Menu.MENU_LOGOUT + Menu.MENU_SECRET + Menu.MENU_END);
					out.write("Hello " + dbOperator.getUserName() + ", you are successfully authorized");
					// session.invalidate();

				} else {
					out.write("Access denied");
					doGet(request, response);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
