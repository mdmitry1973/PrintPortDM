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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddDevice
 */
@WebServlet("/AddDevice")
public class AddDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDevice() {
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
		
			/*
			ID varchar(255),
			UsertID varchar(255),
			Brand varchar(255),
			Model varchar(255),
			Name varchar(255),
			Settings CLOB(64000)
			*/
			
			try {
				
				String strBrand = request.getParameter("selBrand");
				String strModel = request.getParameter("selDevice");
				String strUserId = strIdDB;
				String strName = strModel;
				String strSettings = "";
				UUID deviceID = java.util.UUID.randomUUID();
				String strDeviceID = deviceID.toString();
				int index = 1;
				String strNameDB = "";
				
				if (strBrand == null || strModel == null)
				{
					throw new Exception("strBrand empty");
				}
				
				do{
					
					ResultSet rs;
					Statement stmt;
					
					String selectTableSQL = "SELECT * FROM DEVICES\n WHERE NAME='" + strName + "'";
					
					stmt = DataAccessObject.getConnection().createStatement();
					
					rs = stmt.executeQuery(selectTableSQL);
					
					strNameDB = "";
					
					while (rs.next()) {
						strNameDB = rs.getString("NAME");
					}
				
					if (!strNameDB.isEmpty())
					{
						strName = strModel + " " + index;
					}
				}while(!strNameDB.isEmpty());
				
				String insertTableSQL = "INSERT INTO DEVICES\n"
						+ "VALUES ('" + strDeviceID + "','" + strUserId + "','" + strBrand + "','" + strModel + "','" + strName + "','" + strSettings + "')";
				ResultSet rs;
				Statement stmt = DataAccessObject.getConnection().createStatement();
				
				stmt.executeUpdate(insertTableSQL);
				
				String newUrl = request.getContextPath() + "/MainPrintPort";
				response.sendRedirect(newUrl); 
				  
			} catch (Exception e) {
				//todo something in case error
				String newUrl = request.getContextPath() + "/MainPrintPort.jsp";
				response.sendRedirect(newUrl); 
				e.printStackTrace();
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
