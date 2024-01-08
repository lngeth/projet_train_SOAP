package train;

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

public class Stations extends ServerResource {
	@Get("json")
	public Representation getAllTrains() {
		try {			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
			
			// Prepare query
			String selectQuery = "SELECT * from station";			
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
			
			// Execute query
			ResultSet rs = preparedStatement.executeQuery();
			
			JSONObject toReturn = new JSONObject();
			JSONArray tabResult = new JSONArray();
			
			toReturn.put("status", "200");
			
			if (rs.next()){
				do {		
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", rs.getString(1));
					jsonObject.put("name", rs.getString(2));
					tabResult.put(jsonObject);
				} while(rs.next());
			}
			toReturn.put("listStation", tabResult);
			
			JsonRepresentation jsonRepresentation = new JsonRepresentation(toReturn);
			
			con.close();
			return jsonRepresentation;
		} catch(Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "500");
			jsonObject.put("listStation", "");
			return new JsonRepresentation(jsonObject);
		}
	}
}
