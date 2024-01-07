package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TrainFiltering extends ServerResource {
	@Get
	public String getAllTrains() {
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
			String selectQuery = "SELECT t.id as idTrain, t.maxFirstClassePlaces\r\n" + 
					"FROM voyage as v " + 
					"INNER JOIN train as " + 
					"	ON t.id = v.idTrain " + 
					"INNER JOIN billet as b " + 
					"	ON b.idVoyage = v.id " + 
					"WHERE v.idStationDepart = ? AND v.idStationArrivee = ? AND v.dateDepart = ? AND v.dateArrivee = ? AND t." + travelSQL + " >= ";
			selectQuery += "(SELECT count(b.id) FROM voyage as v " + 
					"INNER JOIN train as t " + 
					"ON t.id = v.idTrain " + 
					"INNER JOIN billet as b " + 
					"ON b.idVoyage = v.id " + 
					"WHERE v.idStationDepart = ? AND v.idStationArrivee = ? AND b.classe = ?) + ?;";
			PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idDeparture);
            preparedStatement.setInt(2, idArrival);
            preparedStatement.setString(3, outboundDateTime);
            preparedStatement.setString(4, returnDateTime);
			
			// Execute query
			ResultSet rs = preparedStatement.executeQuery();
			String res = "Connexion réussie \n";
			if (!rs.next()) {
				res = "No available trains";
			} else {
				do {			
					res += "Train numéro " + rs.getInt(1) + " : " + rs.getString(2) + " : " + rs.getInt(3) + " : " + rs.getInt(4) + " : " + rs.getInt(5) + "\n";  
				} while(rs.next());
			}
			con.close();
			return res;
		} catch(Exception e) {
			System.out.println(e);
			return "Request error";
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
