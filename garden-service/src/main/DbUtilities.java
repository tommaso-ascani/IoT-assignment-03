package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DbUtilities {
	
	static Connection con;
	static HashMap<String, String> data = new HashMap<String, String>();
	
	public DbUtilities() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/garden_service","root","");
		    System.out.println("Db creato");

		   
	   	} catch(Exception e) {
	   		
	   		System.out.println(e);
	   		System.out.println("Errore DB");
	    }  
	}  
	
	//-----------------------------------------------------------------------------------------------------
	
	public void changeValue(String device, int x) throws SQLException {
		String query = "UPDATE garden_service.info SET val=? WHERE device = ?";
	    
	    PreparedStatement preparedStmt = con.prepareStatement(query);
	    preparedStmt.setInt(1, x);
	    preparedStmt.setString(2, device);
	    
	    preparedStmt.executeUpdate();
	    getValue();
	}
	
	public void getValue() throws SQLException {
		Statement statement = con.createStatement();

        ResultSet results = statement.executeQuery("SELECT * FROM garden_service.info");



        while (results.next()) {
              data.put(results.getString(1), results.getString(2));
            }
	}
}
