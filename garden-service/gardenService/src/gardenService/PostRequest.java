package gardenService;

import java.sql.*;

public class PostRequest {
	
	static Connection con;
	
	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");  
		    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/garden_service","root","");  
		   
	    	changeValue("irrigationValue", 0);  
		    
		    con.close();
		   
	   	} catch(Exception e) {
	   		System.out.println(e);
	    }  
	}  
	
	//-----------------------------------------------------------------------------------------------------
	
	public static void changeValue(String device, int x) throws SQLException {
		String query = "UPDATE garden_service.info SET val=? WHERE device = ?";
	    
	    PreparedStatement preparedStmt = con.prepareStatement(query);
	    preparedStmt.setInt(1, x);
	    preparedStmt.setString(2, device);
	    
	    preparedStmt.executeUpdate();
	}
}