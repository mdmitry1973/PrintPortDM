package dm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tomcat.util.http.fileupload.ParameterParser;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Servlet implementation class AddJob
 */
@WebServlet("/AddJob")
@MultipartConfig
public class AddJob extends HttpServlet {
private static final long serialVersionUID = 1L;

private static String jobsFolder = "D:\\my\\Jobs";
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			
		/*
			ID varchar(255),
			UserID varchar(255),
			DeviceID varchar(255),
			Settings CLOB(64000)
		 */
		
		// Check that we have a file upload request
		boolean  isMultipart = ServletFileUpload.isMultipartContent(request);

	      if( !isMultipart )
	      {
	    	  throw new Exception("Not Multipart upload file");
	      }
	      
	      HttpSession session = request.getSession();
	      String strIdDB = (String) session.getAttribute("userId");
	      String strDevice = request.getParameter("selDevice");
	      String strDeviceID = "";
	      
	      if (strIdDB.length() == 0)
	      {
	    	  throw new Exception("userId empty");
	      }
	      
	      {
				ResultSet rs;
				Statement stmt;
				
				String selectTableSQL = "SELECT * FROM DEVICES\n WHERE NAME='" + strDevice + "'";
				
				stmt = DataAccessObject.getConnection().createStatement();
				
				rs = stmt.executeQuery(selectTableSQL);
				
				while (rs.next()) {
					strDeviceID = rs.getString("ID");
				}
	      }
	      
	      if (strDeviceID.length() == 0)
	      {
	    	  throw new Exception("deviceId empty");
	      }
	      
	      Collection<Part> parts = request.getParts();
	      List<Part> fileParts = parts.stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
	      
	      for (Part filePart : fileParts) 
	      {
	    	  String cd= filePart.getHeader("Content-Disposition");
	    	  String strFilePath = "";
	    	  UUID jobID = java.util.UUID.randomUUID();
	    	  String strJobID = jobID.toString();
	    	  
			  if (cd != null) 
			  {
				    String cdl=cd.toLowerCase(Locale.ENGLISH);
				    if (cdl.startsWith("form-data") || cdl.startsWith("attachment")) 
				    {
				      ParameterParser paramParser=new ParameterParser();
				      paramParser.setLowerCaseNames(true);
				      Map<String,String> params=paramParser.parse(cd,';');
				      if (params.containsKey("filename")) 
				      {
							strFilePath=params.get("filename");
							if (strFilePath != null) 
							{
								strFilePath=strFilePath.trim();
							}
							else 
							{
								 strFilePath="";
							}
				        }
				    }
			  }
	    	  
	    	  String fileName = new File(strFilePath).getName();//Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	          InputStream fileContent = filePart.getInputStream();
	          File file = null;
	          File jobsFolderPath = new File(jobsFolder);
	          String fileNameDB = "";
	          
	          if (!jobsFolderPath.exists())
	          {
	        	  jobsFolderPath.mkdirs();
	          }
	          
	          File jobFolderPath = new File(jobsFolder + "\\" + strJobID);
	          
	          if (!jobFolderPath.exists())
	          {
	        	  jobFolderPath.mkdirs();
	          }
	          
	          file = new File(jobFolderPath + "\\job_file");
	         
	          	// Write the file
	            if( fileName.lastIndexOf("\\") >= 0 ){
	               //file = new File(jobFolderPath);// jobsFolder + "\\" + 
	            	fileNameDB = fileName.substring( fileName.lastIndexOf("\\"));
	            }else{
	               //file = new File( jobsFolder + "\\" + 
	            	fileNameDB = fileName.substring(fileName.lastIndexOf("\\")+1);
	            }
	            
	            if (file.exists())
	            {
	            	file.delete();
	            }
	            
	            copyInputStreamToFile(fileContent, file);
	            
	            {
	            	String settings = "";
	            	
	            	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        		
	        		Document doc = docBuilder.newDocument();
	        		Element rootElement = doc.createElement("job_settings");
	        		doc.appendChild(rootElement);
	        		
	        		Attr attrName = doc.createAttribute("name");
	        		attrName.setValue(fileNameDB);
	        		rootElement.setAttributeNode(attrName);
	        		
	        		Attr attrLocalPath = doc.createAttribute("localPath");
	        		attrLocalPath.setValue(fileName);
	        		rootElement.setAttributeNode(attrLocalPath);
	        		
	        		{
	        			TransformerFactory tf = TransformerFactory.newInstance();
	        			Transformer transformer = tf.newTransformer();
	        			//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        			StringWriter writer = new StringWriter();
	        			transformer.transform(new DOMSource(doc), new StreamResult(writer));
	        			settings = writer.getBuffer().toString().replaceAll("\n|\r", "");
	        		}
	            	
	            	String insertTableSQL = "INSERT INTO JOBS\n"
							+ "VALUES ('" + strJobID + "','" + strIdDB + "','" + strDeviceID + "','" + settings + "')";
					
					Statement stmt = DataAccessObject.getConnection().createStatement();
					
					stmt.executeUpdate(insertTableSQL);
	            }
	      }
	      
	     String newUrl = request.getContextPath() + "/MainPrintPort";
	     response.sendRedirect(newUrl); 

	   }
		catch(Exception ex) 
		{
			 
		   System.out.println(ex);
		   
		   //String newUrl = request.getContextPath() + "/MainPrintPort";
    	   //response.sendRedirect(newUrl);
		   
		   request.setAttribute("messageWarning", ex.getMessage());
    	   request.setAttribute("messageWarningType", "alert alert-danger");
    	   request.setAttribute("messageWarningTitle", "Error");
    	   
    	   //response.sendRedirect(newUrl);
    	   
    	   request.getRequestDispatcher("/MainPrintPort").forward(request, response);
    	   
//		   RequestDispatcher servlet;
//		    
//		   ServletContext context = request.getServletContext();
//		   servlet = context.getRequestDispatcher("/MainPrintPort");
//	    
//		   servlet.include(request, response);
	   }
	}

	private void copyInputStreamToFile( InputStream in, File file ) throws Exception {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new Exception("Not Multipart upload file: copyInputStreamToFile");
	    }
	}
}
