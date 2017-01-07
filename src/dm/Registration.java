package dm;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			/*
			ID varchar(255),
			Name varchar(255),
			Password varchar(255)
			*/
			
			String strEmail = request.getParameter("inputEmail");
			String strPassword = request.getParameter("inputPassword");
			String strPasswordConfirm = request.getParameter("inputPasswordConfirm");
			UUID userID = java.util.UUID.randomUUID();
			String strUserID = userID.toString();
			
			//todo need check that user not exist
			//todo need check that password and confirm the same
			
			String insertTableSQL = "INSERT INTO ACCOUNT\n"
					+ "VALUES ('" + strUserID + "','" + strPassword + "','" + strEmail + "')";
			ResultSet rs;
			Statement stmt = DataAccessObject.getConnection().createStatement();
			
			stmt.executeUpdate(insertTableSQL);
			
			//todo check that record was done
			String newUrl = request.getContextPath() + "/index.jsp";
			response.sendRedirect(newUrl);  
		} catch (Exception e) {
			//todo something in case error
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
