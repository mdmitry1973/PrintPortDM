package dm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MainPrintPort
 */
@WebServlet("/MainPrintPort")
public class MainPrintPort extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPrintPort() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
		
			HttpSession session = request.getSession();
	    
			String strIdDB = (String) session.getAttribute("userId");
			
			if (strIdDB.length() == 0)
			{
				throw new Exception("userId empty");
			}
			
			List<String> printersItems = new ArrayList<String>();
			String selectTableSQL = "SELECT * FROM DEVICES\n WHERE USERID='" + strIdDB + "'";
			
			try {
				
				ResultSet rs;
				Statement stmt;
				
				stmt = DataAccessObject.getConnection().createStatement();
				
				rs = stmt.executeQuery(selectTableSQL);
				
				while (rs.next()) 
				{
					printersItems.add(rs.getString("NAME"));
				}
			} 
			catch (Exception e) 
			{
				//todo something in case error
				String newUrl = request.getContextPath() + "/index.jsp";
				response.sendRedirect(newUrl); 
				e.printStackTrace();
			}
			
		    request.setAttribute("printersItems", printersItems);
		    
		    RequestDispatcher jsp;
		    
		    ServletContext context = request.getServletContext();
		    jsp = context.getRequestDispatcher("/MainPrintPort.jsp");
		    
		    jsp.forward(request, response);
			
		} catch (Exception e) {
			//todo something in case error
			String newUrl = request.getContextPath() + "/index.jsp";
			response.sendRedirect(newUrl); 
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
