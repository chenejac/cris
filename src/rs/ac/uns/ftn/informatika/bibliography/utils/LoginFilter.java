package rs.ac.uns.ftn.informatika.bibliography.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rs.ac.uns.ftn.informatika.bibliography.jsf.managedbeans.UserManagedBean;






/**
* chenejac@uns.ac.rs
*
*/

public class LoginFilter implements Filter {

	// This should be your default Home or Login page
	// "login.seam" if you use Jboss Seam otherwise "login.jsf" 
	// "login.xhtml" or whatever
	
	private String loginPage = "/administration/login.jsf";
	
	public void init(FilterConfig filterConfig) throws ServletException {
	
	}
	
	public void doFilter(ServletRequest request, 
		ServletResponse response, FilterChain filterChain) throws IOException,ServletException {

		 HttpServletRequest req = (HttpServletRequest) request;
		 UserManagedBean mb = (UserManagedBean) req.getSession().getAttribute("userManagedBean");
		 if((mb == null) || (! mb.isLoggedIn())){
				if ((request instanceof HttpServletRequest) 
						&& (response instanceof HttpServletResponse)) {
					HttpServletResponse res = (HttpServletResponse) response;
					res.sendRedirect(req.getContextPath() + loginPage);
					
					return;
				}
		 }
		filterChain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}
}
