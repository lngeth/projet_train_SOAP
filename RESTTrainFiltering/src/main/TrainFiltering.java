package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TrainFiltering extends ServerResource {
	@Get
	public String getAllTrains() {
		try {			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
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
