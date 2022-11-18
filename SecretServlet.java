package itea.web07;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecretServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6133996855302631136L;
	private PrintWriter out;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
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
			out.write(Menu.MENU_START + Menu.MENU_LOGOUT + Menu.MENU_END);
			out.write("This is a secret text only for authorized users");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
