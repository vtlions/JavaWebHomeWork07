package itea.web07;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationErrorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8442583412292876543L;
	PrintWriter out;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		writeMessage(response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		writeMessage(response);
	}

	private void writeMessage(HttpServletResponse response) {
		try {
			response.setContentType("text/html");
			out = response.getWriter();
			out.write(Menu.MENU_START + Menu.MENU_LOGIN + Menu.MENU_REGISTRATION + Menu.MENU_END);
			out.write("Access to SecretServlet is denied, you are not authorized");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
