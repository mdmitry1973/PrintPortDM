package dm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strEmail = request.getParameter("inputEmail");
		String strPassword = request.getParameter("inputPassword");
		String strNameDB = "";
		String strPasswordDB = "";
		String strIdDB = "";
		
		String selectTableSQL = "SELECT * FROM ACCOUNT\n WHERE NAME='" + strEmail + "'";
		
		try {
			
			ResultSet rs;
			Statement stmt;
			
			stmt = DataAccessObject.getConnection().createStatement();
			
			rs = stmt.executeQuery(selectTableSQL);
			
			while (rs.next()) {
				strNameDB = rs.getString("NAME");
				strPasswordDB = rs.getString("PASSWORD");
				strIdDB = rs.getString("ID");
			}
		
			if (strEmail.contains(strNameDB)  && 
				strPassword.contains(strPasswordDB) && 
				strIdDB.length() > 0)
			{
				HttpSession session = request.getSession();
			    
			    session.setAttribute("userId", strIdDB);
			    
			    String newUrl = request.getContextPath() + "/MainPrintPort";
				response.sendRedirect(newUrl); 
			}
			else
			{
				//todo need inform that password or email is wrong
				String newUrl = request.getContextPath() + "/index.jsp";
				response.sendRedirect(newUrl); 
			}
		
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
