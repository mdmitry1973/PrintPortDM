package dm;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PrintJobThread extends Thread {
	String jobId;
	PrintJobThread(String jobId) {
        this.jobId = jobId;
    }
	
	public void changeJobStatus(String strStatus, String strMessage)
	{
		String settings = "";
    	
    	//get job info
    	{
			String selectTableSQL = "SELECT * FROM JOBS\n WHERE ID='" + jobId + "'";
			
			try {
				
				ResultSet rs;
				Statement stmt;
				
				stmt = DataAccessObject.getConnection().createStatement();
				
				rs = stmt.executeQuery(selectTableSQL);
				
				while (rs.next()) 
				{
					settings = rs.getString("SETTINGS");
				}
			} 
			catch (Exception e) 
			{
				//todo something in case error
				//String newUrl = request.getContextPath() + "/index.jsp";
				//response.sendRedirect(newUrl); 
				e.printStackTrace();
			}
		}
    	
    	if (settings.length() > 0)
    	{
    		try {
	    		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
				factory.setNamespaceAware(true);
				DocumentBuilder builder;
				
				builder = factory.newDocumentBuilder();
				
	
				Document doc = builder.parse(new ByteArrayInputStream(settings.getBytes()));
				Element rootElement = doc.getDocumentElement();
				Attr attrStatus = rootElement.getAttributeNode("status");
				Attr attrMessage = rootElement.getAttributeNode("message");
				
				if (attrStatus == null)
				{
					attrStatus = doc.createAttribute("status");
				}
				
				if (attrMessage == null)
				{
					attrMessage = doc.createAttribute("message");
				}
				
				attrStatus.setValue(strStatus);
				rootElement.setAttributeNode(attrStatus);
				attrMessage.setValue(strMessage);
				rootElement.setAttributeNode(attrMessage);
				
				{
        			TransformerFactory tf = TransformerFactory.newInstance();
        			Transformer transformer = tf.newTransformer();
        			//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        			StringWriter writer = new StringWriter();
        			transformer.transform(new DOMSource(doc), new StreamResult(writer));
        			settings = writer.getBuffer().toString().replaceAll("\n|\r", "");
        		}
				
				String selectTableSQL = "UPDATE JOBS SET SETTINGS='" + settings + "' WHERE ID='" + jobId + "'";
				
				Statement stmt = DataAccessObject.getConnection().createStatement();
				
				stmt.executeUpdate(selectTableSQL);
				
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}

    public void run() {
    	
    	changeJobStatus("PRINTING", "Printing %0");
    	
    	String deviceID = "";
    	String settings = "";
    	String modle = "";
    	String brand = "";
    	String settingsDevice = "";
    	
    	//get job info
    	{
			String selectTableSQL = "SELECT * FROM JOBS\n WHERE ID='" + jobId + "'";
			
			try {
				
				ResultSet rs;
				Statement stmt;
				
				stmt = DataAccessObject.getConnection().createStatement();
				
				rs = stmt.executeQuery(selectTableSQL);
				
				while (rs.next()) 
				{
					settings = rs.getString("SETTINGS");
					deviceID = rs.getString("DEVICEID");
				}
			} 
			catch (Exception e) 
			{
				//todo something in case error
				//String newUrl = request.getContextPath() + "/index.jsp";
				//response.sendRedirect(newUrl); 
				e.printStackTrace();
			}
		}
    	
    	//get device info
    	{
			String selectTableSQL = "SELECT * FROM DEVICES\n WHERE ID='" + deviceID + "'";
			
			try {
				
				ResultSet rs;
				Statement stmt;
				
				stmt = DataAccessObject.getConnection().createStatement();
				
				rs = stmt.executeQuery(selectTableSQL);
				
				while (rs.next()) 
				{
					modle = rs.getString("MODEL");
					brand = rs.getString("BRAND");
					settingsDevice = rs.getString("SETTINGS");
				}
			} 
			catch (Exception e) 
			{
				//todo something in case error
				//String newUrl = request.getContextPath() + "/index.jsp";
				//response.sendRedirect(newUrl); 
				e.printStackTrace();
			}
		}
    	
    	RowDriver rowDriver = new RowDriver(brand, modle, settingsDevice);
    	String status = "";
    	boolean res = true;
    	
    	rowDriver.createInstance();
    	
    	rowDriver.sendJob(jobId, settings);
    	
    	while(res)
    	{
    		res = rowDriver.progress(status);
    	}
    	
    	rowDriver.removeInstance();
    	
    	changeJobStatus("DONE", "Done");
    	
    	JobThreadManager.getInstance().removePrintThread(this);
    }
}
