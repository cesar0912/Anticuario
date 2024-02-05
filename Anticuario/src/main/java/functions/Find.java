package functions;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import dao.MongoDB;

public class Find {
	public static void FindAll() {
		try {
            MongoClient mongoClient = MongoDB.getClient();
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");
            
            // Obtener todos los documentos de la colección
            MongoCursor<Document> cursor = collection.find().iterator();

            // Iterar y mostrar cada documento
            try {
            	Document document = null;
                while (cursor.hasNext()) {
                    document = cursor.next();
                    System.out.println(pretty(document.toJson()));
                }
                
            } finally {
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static String pretty(String json) {
		JsonElement je = JsonParser.parseString(json);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(je);
	}
}
