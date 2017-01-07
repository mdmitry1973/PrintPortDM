package dm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.ParameterParser;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class AddJob
 */
@WebServlet("/AddJob")
public class AddJob extends HttpServlet {
private static final long serialVersionUID = 1L;
	
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
	      response.setContentType("text/html");
	      java.io.PrintWriter out = response.getWriter( );
	      if( !isMultipart )
	      {
	    	//todo something in case error
	         out.println("<html>");
	         out.println("<head>");
	         out.println("<title>Servlet upload</title>");  
	         out.println("</head>");
	         out.println("<body>");
	         out.println("<p>No file uploaded</p>"); 
	         out.println("</body>");
	         out.println("</html>");
	         return;
	      }
	      
	      List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
	      
	      out.println("<html>");
	      out.println("<head>");
	      out.println("<title>Servlet upload</title>");  
	      out.println("</head>");
	      out.println("<body>");
	      
	      for (Part filePart : fileParts) 
	      {
	    	  String cd= filePart.getHeader("Content-Disposition");
	    	  String strFilePath = "";
	    	  
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
	         
	          	// Write the file
	            if( fileName.lastIndexOf("\\") >= 0 ){
	               file = new File( "c:\\temp\\" + 
	               fileName.substring( fileName.lastIndexOf("\\"))) ;
	            }else{
	               file = new File( "c:\\temp\\" + 
	               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	            }
	            
	            if (file.exists())
	            {
	            	file.delete();
	            }
	            
	            copyInputStreamToFile(fileContent, file);
	            
	            out.println("Uploaded Filename: " + fileName + "<br>");
	      }
	      
	      out.println("</body>");
	      out.println("</html>");
	      
	     String newUrl = request.getContextPath() + "/MainPrintPort";
	     response.sendRedirect(newUrl); 

	   }
		catch(Exception ex) 
		{
		   //todo something in case error
		   System.out.println(ex);
	   }
	}

	private void copyInputStreamToFile( InputStream in, File file ) {
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
	    }
	}
}
