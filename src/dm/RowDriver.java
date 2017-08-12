package dm;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import dm.RowDriver.ROWDRIVERDLL;

public class RowDriver  {
	
	public String brand;
	public String  model;
	public String  settingsDevice;
	public ROWDRIVERDLL sdll;
	
	public interface ROWDRIVERDLL extends Library {
		
		ROWDRIVERDLL INSTANCE = (ROWDRIVERDLL) Native.loadLibrary("ROWDRIVERDLL", ROWDRIVERDLL.class);
        
		int createInstance(String brand, String  model, String  settingsDevice);
		int removeInstance();
		int sendJob(String jobId, String jobInfo);
		int progress(PointerByReference  status);
    }
	
	RowDriver(String brand, String  model, String settingsDevice)
	{
		this.brand = brand;
		this.model = model;
		this.settingsDevice = settingsDevice;
	}
	
	public boolean createInstance()
	{
		//Runtime.getRuntime().load("D:\\my\\PrinterDriversWrap\\EPHRDLL\\Debug\\EPHRDLL.dll");
		Runtime.getRuntime().load("D:\\my\\PrinterDriversWrap\\libs\\ROWDRIVERDLL.dll");
		
		sdll = ROWDRIVERDLL.INSTANCE;
        
        //String initString = "HTM2LIB=D:\\my\\PrinterDriversWrap\\libs\\EPHR64_KG02.dll\nINIFILE=D:\\my\\PrinterDriversWrap\\libs\\EPHR_MODULE_W64.ini\n";
        //PointerByReference output = new PointerByReference();
        
        int resFN = sdll.createInstance(brand, model, settingsDevice);
		        
		return true;
	}
	
	public boolean removeInstance()
	{
		int resFN = sdll.removeInstance();
		        
		return true;
	}
	
	public boolean progress(String  status)
	{
		int resFN = 0;
		PointerByReference output = new PointerByReference();
		Pointer p =	output.getValue();
		
        resFN = sdll.progress(output);
        	
        status = p.getString(0);
        	
		return true;
	}
	
	public boolean sendJob(String jobId, String jobInfo)
	{
		boolean res = true;
		int pageWidth = 200;
		int pageLength = 300;
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("D:\\dev\\app11\\apps\\Application\\Samples\\Tiffs\\Indian.tif"));
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		pageWidth = img.getWidth();
		pageLength = img.getHeight();
        
        int resFN = sdll.sendJob(jobId, jobInfo);
        
		return res;
	}
	
}
