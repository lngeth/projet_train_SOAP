package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class CreateClient extends ServerResource {
	@Post
	public Representation createClientByName(Form form) {
		try {		
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
			
			// Retrieve parameters
			String name = (String) form.getValues("name");
			
			// Return variable
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "200");
			
			if (name.equals("")) {
				jsonObject.put("success", false);
				return new JsonRepresentation(jsonObject);
			}

			// Check if already in database
			String selectQuery = "SELECT * FROM client WHERE nom = ?;";
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);

			ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next()) {
				jsonObject.put("success", false);
				return new JsonRepresentation(jsonObject);
			}

			String insertQuery = "INSERT INTO client (nom) VALUES (?);";
			preparedStatement = con.prepareStatement(insertQuery);
			preparedStatement.setString(1, name);
			// Execute query
			int insertRes = preparedStatement.executeUpdate();

			if (insertRes == 1) {
				jsonObject.put("success", true);
			} else {
				jsonObject.put("success", false);
			}
			
			con.close();
			return new JsonRepresentation(jsonObject);
		} catch(Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "500");
			jsonObject.put("success", false);
			return new JsonRepresentation(jsonObject);
		}
	}
}
