package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;
import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class User extends ServerResource {
	@Get("json")
	public Representation getAllTrains() {
		try {			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");

      String name = (String) getRequestAttributes().get("name");
      
			// Prepare query
			String selectQuery = "SELECT * from client WHERE nom = ?;";			
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
      preparedStatement.setString(1, name);
			
			// Execute query
			ResultSet rs = preparedStatement.executeQuery();
			
			JSONObject toReturn = new JSONObject();
			toReturn.put("status", "200");

			if (rs.next()){
        toReturn.put("idClient", Integer.toString(rs.getInt(1)));
			} else {
        toReturn.put("idClient", "");
      }
			
			JsonRepresentation jsonRepresentation = new JsonRepresentation(toReturn);
			
			con.close();
			return jsonRepresentation;
		} catch(Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "500");
			jsonObject.put("idClient", "");
			return new JsonRepresentation(jsonObject);
		}
	}
}
