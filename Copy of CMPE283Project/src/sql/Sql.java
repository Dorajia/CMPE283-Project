package sql;


import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.jdbc.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Sql {
  final String databaseURL = "jdbc:mysql://cmpe273-project.cjnlvg1wtyvb.us-east-1.rds.amazonaws.com:3306";
  final static String user = "cmpe273";
  final static String password = "cmpe2833";

  Connection connection;
  PreparedStatement preparedStatement;
  ResultSet rs;
  String TaskID=null;

  public Sql() throws Exception{
    Class.forName("com.mysql.jdbc.Driver").newInstance();
  }
  
  public void insertTaskData(String targetname, String res, String des, String taskname,String status) {
    try {
      connection = DriverManager.getConnection(databaseURL, user, password);
      preparedStatement = connection.prepareStatement("INSERT INTO cmpe283.TASK_TABLE (TARGET_NAME, RES,DES,TASK_NAME,T_STATUS) VALUES (?, ?,?,?,?);");
      preparedStatement.setString(1, targetname);
      preparedStatement.setString(2, res);
      preparedStatement.setString(3, des);
      preparedStatement.setString(4, taskname);
      preparedStatement.setString(5, status);
      preparedStatement.executeUpdate();
    } catch (java.sql.SQLException ex) {
    	System.out.println(ex.getMessage());
    } finally {
      try {
    	  TaskID= preparedStatement.getGeneratedKeys().getString(0);
        preparedStatement.close();
      } catch (java.sql.SQLException ex) {
        preparedStatement = null;
      }
      try {
        connection.close();
      } catch (java.sql.SQLException ex) {
        connection = null;
      }
    }
  }
  
  public void insertEventData(String desc,String targetname, String eventname,String status) {
	    try {
	      connection = DriverManager.getConnection(databaseURL, user, password);
	      preparedStatement = connection.prepareStatement("INSERT INTO cmpe283.EVENT_TABLE (DESCRIPTION, TID,TARGET_NAME,EVENT_NAME,E_STATUS) VALUES (?, ?,?,?,?);");
	      preparedStatement.setString(1, desc);
	      preparedStatement.setString(2, TaskID);
	      preparedStatement.setString(3, targetname);
	      preparedStatement.setString(4, eventname);
	      preparedStatement.setString(5, status);
	      preparedStatement.executeUpdate();
	    } catch (java.sql.SQLException ex) {
	    	System.out.println(ex.getMessage());
	    } finally {
	      try {
	    	  
	        preparedStatement.close();
	      } catch (java.sql.SQLException ex) {
	        preparedStatement = null;
	      }
	      try {
	        connection.close();
	      } catch (java.sql.SQLException ex) {
	        connection = null;
	      }
	    }
	  }
  	public void updateTaskData(String status){
  		try {
  	      connection = DriverManager.getConnection(databaseURL, user, password);
  	      preparedStatement = connection.prepareStatement("UPDATE cmpe283.TASK_TABLE SET T_STATUS= ?, END_TS = current_timestamp() WHERE TID = ?;");
	      preparedStatement.setString(1, status);
	      preparedStatement.setString(2, TaskID);
	      preparedStatement.executeUpdate();
	      
	      
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	}
//  public static List<List<String>> getData() {
//    List<ArrayList<String>> toReturn = new ArrayList<ArrayList<String>>();
//    try {
//      connection = DriverManager.getConnection(databaseURL, user, password);
//      preparedStatement = connection.prepareStatement("SELECT * FROM Shippable.test");
//      rs = preparedStatement.executeQuery();
//      if (rs.next()) {
//        toReturn = rs.getString("city") + ", " + rs.getString("state");
//      }
//    } catch (java.sql.SQLException ex) {
//    } finally {
//      try {
//        preparedStatement.close();
//      } catch (java.sql.SQLException ex) {
//        preparedStatement = null;
//      }
//      try {
//        connection.close();
//      } catch (java.sql.SQLException ex) {
//        connection = null;
//      }
//      return toReturn;
//    }
//  }
}