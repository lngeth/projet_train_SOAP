package train;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TrainReservation extends ServerResource {
	@Get("json")
	public Representation reserveTrain() {
		try {			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
			
			// Retrieve parameters
			int idClient = Integer.parseInt((String) getRequestAttributes().get("idClient"));
			int flex = Integer.parseInt((String) getRequestAttributes().get("flex"));
			int idVoyage = Integer.parseInt((String) getRequestAttributes().get("idVoyage"));
			String travelClass = (String) getRequestAttributes().get("travelClass");
			
			// Prepare query
			String selectQuery = "INSERT INTO billet (flexible, classe, idClient, idVoyage) VALUES (?,?,?,?);";
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setInt(1, flex);
            preparedStatement.setString(2, travelClass);
            preparedStatement.setInt(3, idClient);
            preparedStatement.setInt(4, idVoyage);
			
			// Execute query
			int rs = preparedStatement.executeUpdate();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "200");
			
			if (rs == 0) {
				jsonObject.put("success", false);
			} else {
				jsonObject.put("success", true);
			}
			
			JsonRepresentation jsonRepresentation = new JsonRepresentation(jsonObject);
			
			con.close();
			return jsonRepresentation;
		} catch(Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", "500");
			jsonObject.put("success", false);
			return new JsonRepresentation(jsonObject);
		}
	}
}
