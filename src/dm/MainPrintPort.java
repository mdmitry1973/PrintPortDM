package dm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
			
			List<ArrayList<String>> jobItems = new ArrayList<ArrayList<String>>();
			List<String> jobHeadItems = new ArrayList<String>();
			List<String> printersItems = new ArrayList<String>();
			List<String> printersIdItems = new ArrayList<String>();
			
			{
				String selectTableSQL = "SELECT * FROM DEVICES\n WHERE USERID='" + strIdDB + "'";
				
				try {
					
					ResultSet rs;
					Statement stmt;
					
					stmt = DataAccessObject.getConnection().createStatement();
					
					rs = stmt.executeQuery(selectTableSQL);
					
					while (rs.next()) 
					{
						printersItems.add(rs.getString("NAME"));
						printersIdItems.add(rs.getString("ID"));
					}
				} 
				catch (Exception e) 
				{
					//todo something in case error
					String newUrl = request.getContextPath() + "/index.jsp";
					response.sendRedirect(newUrl); 
					e.printStackTrace();
				}
			}
			
			jobHeadItems.add("#");
			jobHeadItems.add("Job name");
			jobHeadItems.add("File type");
			jobHeadItems.add("Devcie");
			
			{
				String selectTableSQL = "SELECT * FROM JOBS\n WHERE USERID='" + strIdDB + "'";
				
				try {
					
					ResultSet rs;
					Statement stmt;
					
					stmt = DataAccessObject.getConnection().createStatement();
					
					rs = stmt.executeQuery(selectTableSQL);
					
					while (rs.next()) 
					{
						ArrayList<String> jobData = new ArrayList<String>();
						String settings = rs.getString("SETTINGS");
						String deviceID = rs.getString("DEVICEID");
						String printerName = "";
						Iterator it = printersIdItems.iterator();
						Iterator it1 = printersIdItems.iterator();
						while (it.hasNext() && it1.hasNext())
						{ 
							String newsItem = (String) it.next();
							String newsItemName = (String) it1.next();
		   				
							if (newsItem == deviceID)
							{
								printerName = newsItemName;
								break;
							}
		   				}
						
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

						factory.setNamespaceAware(true);
						DocumentBuilder builder = factory.newDocumentBuilder();

						Document doc = builder.parse(new ByteArrayInputStream(settings.getBytes()));
						Element rootElement = doc.getDocumentElement();
						Attr attrName = rootElement.getAttributeNode("name");
						
						jobData.add("" + (jobItems.size() + 1));
						jobData.add(attrName.getValue());
						jobData.add(attrName.getValue());
						jobData.add(printerName);
						
						jobItems.add(jobData);
					}
				} 
				catch (Exception e) 
				{
					//todo something in case error
					String newUrl = request.getContextPath() + "/index.jsp";
					response.sendRedirect(newUrl); 
					e.printStackTrace();
				}
			}
			
			if (request.getAttribute("messageWarning") == null)
				request.setAttribute("messageWarning", "");
	    	request.setAttribute("messageWarningType", "alert alert-danger");
	    	request.setAttribute("messageWarningTitle", "Error");
	    	  
		    request.setAttribute("printersItems", printersItems);
		    request.setAttribute("jobHeadItems", jobHeadItems);
		    request.setAttribute("jobItems", jobItems);
		    
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
