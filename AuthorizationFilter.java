package itea.web07;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorizationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		System.out.println("doFilter is working");
		HttpSession session = ((HttpServletRequest) request).getSession();

		session.getAttribute(Login.USER_ACCESS_GRANTED);

		if (session.getAttribute(Login.USER_ACCESS_GRANTED) == null) {

			((HttpServletResponse) response).sendRedirect("http://localhost:8080/authorization-error");

			//second method
//			((HttpServletResponse) response).setStatus(((HttpServletResponse) response).SC_MOVED_TEMPORARILY);
//			((HttpServletResponse) response).setHeader("Location", "http://localhost:8080/authorization-error");    

		} else {

			filterChain.doFilter(request, response);

		}
	}

}
