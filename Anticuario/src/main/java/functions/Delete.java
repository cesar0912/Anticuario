package functions;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import dao.MongoDB;

public class Delete {

	

	// Borra objeto por ID
	public static void deleteByID(String id) {
		try {
            MongoClient mongoClient = MongoDB.getClient();
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");

            // Crear un filtro para seleccionar el documento por su ID
            Bson filter = Filters.eq("_id", new ObjectId(id));

            // Eliminar el documento que coincide con el filtro
            DeleteResult result = collection.deleteOne(filter);

            // Imprimir el resultado
            if (result.getDeletedCount() > 0) {
                System.out.println("Documento borrado correctamente en MongoDB.");
            } else {
                System.out.println("No se encontró ningún documento con el ID proporcionado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	// Borra objetos que posean una key concreta
	public static void deleteByKey(String dato) {
		try {
            MongoClient mongoClient = MongoDB.getClient();
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");
            if(!dato.equals("nombre") || !dato.equals("tipo") || !dato.equals("precio")) {
            	Document filter = new Document("propiedades." + dato, new Document("$exists", true));
            	collection.deleteMany(filter);
            }else {
            	Document filter = new Document(dato, new Document("$exists", true));
            	collection.deleteMany(filter);
            }

            System.out.println("Documentos borrados correctamente en MongoDB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	// Borra objetos que posean unos values concretos
	public static void deleteByValues(Document document) {
	    try {
	        MongoClient mongoClient = MongoDB.getClient();
	        MongoDatabase database = mongoClient.getDatabase("Anticuario");
	        MongoCollection<Document> collection = database.getCollection("Productos");

	        List<Bson> filters = new ArrayList<>();
	        for (String key : document.keySet()) {
	            filters.add(Filters.eq(key, document.get(key)));
	        }
	        
	        Bson filter = Filters.or(filters);
	        collection.deleteMany(filter);

	        System.out.println("Documentos borrados correctamente en MongoDB.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
