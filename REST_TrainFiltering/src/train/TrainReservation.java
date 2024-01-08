package train;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
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
			String selectQuery = "SELECT t.id as idTrain, t.nom, v.dateDepart, v.id as idVoyage, v.prixStandard, v.prixFirst, v.prixPremium, v.prixFlexibilite " + 
					"FROM voyage as v " + 
					"INNER JOIN train as t " + 
					"ON t.id = v.idTrain " + 
					"LEFT JOIN billet as b " + 
					"ON b.idVoyage = v.id " + 
					"WHERE ";
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idDeparture);
			
			// Execute query
			ResultSet rs = preparedStatement.executeQuery();
			JSONArray jsonArray = new JSONArray();
			
			if (!rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("error", "No available trains");
				jsonArray.put(jsonObject);
			} else {
				do {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", rs.getInt(1));
					jsonObject.put("name", rs.getString(2));
					jsonObject.put("dateDepart", rs.getString(3));
					jsonObject.put("idVoyage", rs.getInt(4));
					jsonObject.put("prixStandard", rs.getInt(5));
					jsonObject.put("prixFirst", rs.getInt(6));
					jsonObject.put("prixPremium", rs.getInt(7));
					jsonObject.put("prixFlexibilite", rs.getInt(8));
					jsonArray.put(jsonObject);
				} while(rs.next());
			}
			
			JsonRepresentation jsonRepresentation = new JsonRepresentation(jsonArray);
			
			con.close();
			return jsonRepresentation;
		} catch(Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("error", "Error request");
			return new JsonRepresentation(jsonObject);
		}
	}
}
