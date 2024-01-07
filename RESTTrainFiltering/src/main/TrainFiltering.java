package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TrainFiltering extends ServerResource {
	@Get
	public String getAllTrains() {
		try {			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
			
			// Retrieve parameters
			String departure = (String) getRequestAttributes().get("departure");
			String arrival = (String) getRequestAttributes().get("arrival");
			String outboundDateTime = (String) getRequestAttributes().get("outboundDateTime");
			String returnDateTime = (String) getRequestAttributes().get("returnDateTime");
			int nbTickets = (int) getRequestAttributes().get("nbTickets");
			String travelClass = (String) getRequestAttributes().get("travelClass");
			
			// Prepare query
			String selectQuery = "SELECT * FROM train WHERE id = ?";
            preparedStatement = connexion.prepareStatement(selectQuery);
            preparedStatement.setInt(1, 1);
			
			// Execute query
			Statement stmt=con.createStatement();  
			
			ResultSet rs=stmt.executeQuery("select * from train");
			String res = "Connexion réussie \n";
			while(rs.next()) {			
				res += "Train numéro " + rs.getInt(1) + " : " + rs.getString(2) + " : " + rs.getInt(3) + " : " + rs.getInt(4) + " : " + rs.getInt(5) + "\n";  
			}
			con.close();
			return res;
		} catch(Exception e) {
			System.out.println(e);
		}
		return "ici";
	}
}
