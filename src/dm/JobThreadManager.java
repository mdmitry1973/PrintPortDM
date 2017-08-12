package dm;

import java.util.ArrayList;
import java.util.List;

public class JobThreadManager extends Thread {

	private static JobThreadManager instance;
	
	private boolean bStopThead;
	private List<PrintJobThread> printJobs;
	
	public static void createInstance()
	{
		instance = new JobThreadManager();
	}
	
	JobThreadManager() 
	{
		bStopThead = false;
		printJobs = new ArrayList<PrintJobThread>();
    }
	
	public static JobThreadManager getInstance()
	{
		return instance;
	}
	
	public void addPrintThread(PrintJobThread thread)
	{
		printJobs.add(thread);
	}
	
	public void removePrintThread(PrintJobThread thread)
	{
		for(int i = 0; i < printJobs.size(); i++)
		{
			if (printJobs.get(i) == thread)
			{
				printJobs.remove(i);
				break;
			}
		}
	}
	
	public void stopThread()
	{
		bStopThead = true;
		
		while(isAlive())
		{
			for(int i = 0; i < printJobs.size(); i++)
			{
				if (!printJobs.get(i).isAlive())//check that thread changed status
				{
					
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run() 
	{
       while (bStopThead)
       {
    	   try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
    }
}
