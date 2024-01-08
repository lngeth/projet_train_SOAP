package train;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;


public class TrainFiltering extends ServerResource {
	@Get("json")
	public Representation getAllTrains() {
		try {			
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
			
			// Retrieve parameters
			int idDeparture = Integer.parseInt((String) getRequestAttributes().get("idDeparture"));
			int idArrival = Integer.parseInt((String) getRequestAttributes().get("idArrival"));
			String outboundDateTime = this.convertTimestampToDatetime((String) getRequestAttributes().get("outboundDateTime"));
			String returnDateTime = this.convertTimestampToDatetime((String) getRequestAttributes().get("returnDateTime"));
			int nbTickets = Integer.parseInt((String) getRequestAttributes().get("nbTickets"));
			String travelClass = (String) getRequestAttributes().get("travelClass");
			
			String travelSQL = "";
			switch (travelClass) {
			case "First":
				travelSQL = "maxFirstClassePlaces";
				break;
			case "Business":
				travelSQL = "maxBusinessClassePlaces";
				break;
			case "Standard":
				travelSQL = "maxStandardClassePlaces";
				break;
			default:
				break;
			}
			
			// Prepare query
			String selectQuery = "SELECT t.id as idTrain, t.nom, v.dateDepart, v.id as idVoyage, v.prixStandard, v.prixFirst, v.prixPremium, v.prixFlexibilite " + 
					"FROM voyage as v " + 
					"INNER JOIN train as t " + 
					"ON t.id = v.idTrain " + 
					"LEFT JOIN billet as b " + 
					"ON b.idVoyage = v.id " + 
					"WHERE ";
			String condition = "(v.idStationDepart = ? AND v.idStationArrivee = ? AND v.dateDepart >= ? AND v.dateDepart < DATE_ADD(?, INTERVAL 1 DAY) AND t." + travelSQL + " >= ";
			condition += "(SELECT count(b.id) FROM voyage as v " + 
					"INNER JOIN train as t " + 
					"ON t.id = v.idTrain " + 
					"LEFT JOIN billet as b " + 
					"ON b.idVoyage = v.id " + 
					"WHERE v.idStationDepart = ? AND v.idStationArrivee = ? AND v.dateDepart >= ? AND v.dateDepart < DATE_ADD(?, INTERVAL 1 DAY) AND b.classe = ?) + ?)";
			selectQuery += condition + " OR " + condition + ";";
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idDeparture);
            preparedStatement.setInt(2, idArrival);
            preparedStatement.setString(3, outboundDateTime);
            preparedStatement.setString(4, outboundDateTime);
            preparedStatement.setInt(5, idDeparture);
            preparedStatement.setInt(6, idArrival);
            preparedStatement.setString(7, outboundDateTime);
            preparedStatement.setString(8, outboundDateTime);
            preparedStatement.setString(9, travelClass);
            preparedStatement.setInt(10, nbTickets);
            preparedStatement.setInt(11, idArrival);
            preparedStatement.setInt(12, idDeparture);
            preparedStatement.setString(13, returnDateTime);
            preparedStatement.setString(14, returnDateTime);
            preparedStatement.setInt(15, idArrival);
            preparedStatement.setInt(16, idDeparture);
            preparedStatement.setString(17, returnDateTime);
            preparedStatement.setString(18, returnDateTime);
            preparedStatement.setString(19, travelClass);
            preparedStatement.setInt(20, nbTickets);
			
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
	
	private String convertTimestampToDatetime(String ts) {
		long timestampInMillis = Long.parseLong(ts);
        Timestamp timestamp = new Timestamp(timestampInMillis);
        Date date = new Date(timestamp.getTime());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
	}
}
