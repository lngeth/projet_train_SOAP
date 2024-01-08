package train;

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

public class TrainReservation extends ServerResource {
	@Post
	public Representation reserveTrain(Form form) {
		try {		
			System.out.println("coucoucouocuouo");
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/train_project","lngeth","0207");
			
			System.out.println("connexion à la BD réussie");
			
			System.out.println("création du form");
			
			// Retrieve parameters
			int nbTickets = Integer.parseInt((String) form.getValues("nbTickets"));
			System.out.println("nb tickets : " + nbTickets);
			String[] idClient = form.getValuesArray("idClient");
			System.out.println("idClient	 1 2 : " + idClient[0] + "/" + idClient[1]);
			String[] flex = form.getValuesArray("flex");
			String[] idVoyage = form.getValuesArray("idVoyage");
			String[] travelClass = form.getValuesArray("travelClass");
			
			System.out.println("idVoyage 1 2 : " + idVoyage[0] + "/" + idVoyage[1]);
			System.out.println("nbtickets : " + nbTickets);
			// Return variable
			JSONObject jsonObject = new JSONObject();
			
			if (idClient.length == flex.length && flex.length == idVoyage.length && idVoyage.length == travelClass.length) {
				jsonObject.put("status", "200");
			} else {
				jsonObject.put("status", "500");
				jsonObject.put("success", false);
				return new JsonRepresentation(jsonObject);
			}
			System.out.println("ici");
			
			PreparedStatement preparedStatement = null;
			// Verify availability of trip
			boolean allGood = true;
			for (int i = 0; i < idClient.length; i++) {
				String travelSQL = "";
				switch (travelClass[i]) {
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
				
				String verifyQuery = "SELECT CASE WHEN (" + 
						"SELECT COUNT(*) FROM billet as b WHERE b.idVoyage = ? AND b.classe = ?) " + 
						"+ ? <= (SELECT t." + travelSQL + " FROM train as t INNER JOIN voyage as v ON v.idTrain = t.id WHERE v.id = ?) " + 
						"THEN 1 ELSE 0 END";
				preparedStatement = con.prepareStatement(verifyQuery);
				preparedStatement.setInt(1, Integer.parseInt(idVoyage[i]));
				preparedStatement.setString(2, travelClass[i]);
				preparedStatement.setInt(3, nbTickets);
				preparedStatement.setInt(4, Integer.parseInt(idVoyage[i]));
				
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next())
					if (rs.getInt(1) == 0) {
						allGood = false;
						break;
					}
			}
			System.out.println("icii");
			
			int[] tabInsertStatus = new int[idClient.length * nbTickets]; 
			if (allGood) { // Execute insert query
				for (int i = 0; i < idClient.length; i++) {
					for (int j = 0; j < nbTickets; j++) {						
						// Prepare insert query
						String insertQuery = "INSERT INTO billet (flexible, classe, idClient, idVoyage) VALUES (?,?,?,?);";
						preparedStatement = con.prepareStatement(insertQuery);
						preparedStatement.setInt(1, Integer.parseInt(flex[i]));
						preparedStatement.setString(2, travelClass[i]);
						preparedStatement.setInt(3, Integer.parseInt(idClient[i]));
						preparedStatement.setInt(4, Integer.parseInt(idVoyage[i]));
						
						// Execute query
						int rs = preparedStatement.executeUpdate();
						
						int index = i+j;
						if (rs == 0) {
							tabInsertStatus[index] = 0;
						} else {
							tabInsertStatus[index] = 1;
						}
					}
				}
				
				boolean allInsertPassed = true;
				for (int i = 0; i < tabInsertStatus.length; i++) {
					if (tabInsertStatus[i] == 0) {
						allInsertPassed = false;
						break;
					}
				}
				
				if (allInsertPassed)
					jsonObject.put("success", true);
				else
					jsonObject.put("success", false);
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
