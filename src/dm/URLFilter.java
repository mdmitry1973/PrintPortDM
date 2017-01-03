package dm;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class URLFilter
 */
@WebFilter("/URLFilter")
public class URLFilter implements Filter {

    /**
     * Default constructor. 
     */
    public URLFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		//String url = req.getRequestURI();
        HttpServletResponse resp = (HttpServletResponse)response;
        String servletPath = req.getServletPath();
        //PrintPortDM/index.jsp
        if (/*req.getRemoteUser() != null &&*/ req.getScheme().equals("http"))// &&
        		//!servletPath.endsWith("index.jsp") && 
        		//!servletPath.contains("bootstrap")) 
        {
            String url = "https://" + req.getServerName() + ":8443"
                    + req.getContextPath() + req.getServletPath();
            if (req.getPathInfo() != null) 
            {
                url += req.getPathInfo();
            }
            resp.sendRedirect(url);
        } 
        else 
        {
            chain.doFilter(request, response);
        }
		// pass the request along the filter chain
		//chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
