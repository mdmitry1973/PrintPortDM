package dm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import javax.sql.DataSource;
import org.apache.derby.jdbc.ClientDriver;
import org.apache.derby.jdbc.EmbeddedDriver;

public class DataAccessObject {

	private static String dbURL = "jdbc:derby://localhost:1527/PrintPort;create=true;user=admin;password=asdcvbnm1";
	private static Connection conn = null;
  // private static Statement stmt = null;

//   public static void setDataSource(DataSource dataSource) 
//   {
//      DataAccessObject.dataSource = dataSource;
//   }
   
   protected static void createConnection()
   {
       try
       {
           Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
           //Get a connection
           conn = DriverManager.getConnection(dbURL); 
       }
       catch (Exception except)
       {
           except.printStackTrace();
       }
   }

   public static Connection getConnection() 
   {
      try {
    	  if (conn == null)
    	  {
    		  createConnection();
    	  }
    	  
         return conn;//dataSource.getConnection();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   public static void shutdown()
   {
       try
       {
          // if (stmt != null)
           ////{
           //    stmt.close();
           //}
           if (conn != null)
           {
               DriverManager.getConnection(dbURL + ";shutdown=true");
               conn.close();
           }           
       }
       catch (SQLException sqlExcept)
       {
           
       }

   }

//   protected static void close(Statement statement, Connection connection) 
//   {
//      close(null, statement, connection);
//   }
//
//   protected static void close(ResultSet rs, Statement statement,
//         Connection connection) 
//   {
//      try {
//         if (rs != null)
//            rs.close();
//         if (statement != null)
//            statement.close();
//         if (connection != null)
//            connection.close();
//      } catch (SQLException e) {
//         throw new RuntimeException(e);
//      }
//   }
}