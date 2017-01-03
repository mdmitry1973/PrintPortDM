package dm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PrintPortContextListener
 *
 */
@WebListener
public class PrintPortContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public PrintPortContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	
    	DataAccessObject.shutdown();
    	System.out.println("Shutting down!");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	//DataAccessObject.getConnection();
    	System.out.println("Starting up!");
    }
	
}
