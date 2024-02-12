package functions;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import dao.MongoDB;

public class Modify {
	public static Document findById(String id) {
		Document result=new Document();
		try {
	            MongoClient mongoClient = MongoDB.getClient();
	            MongoDatabase database = mongoClient.getDatabase("Anticuario");
	            MongoCollection<Document> collection = database.getCollection("Productos");
	
	            ObjectId objectId = new ObjectId(id);
	            Document filter = new Document("_id", objectId);
	
	            Document projection = new Document("propiedades", 1);
	            projection.append("_id", 0); 
	
	            result = collection.find(filter).first();
		 	} catch (Exception e) {
	            e.printStackTrace();
	        }
            if(result!=null) {
            	return result;
            }else {
            	return null;
            }
	}
	public static Document modifyById(String id) {
		try {
            MongoClient mongoClient = MongoDB.getClient();
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");

            ObjectId objectId = new ObjectId(id);
            Document filter = new Document("_id", objectId);

            Document projection = new Document("propiedades", 1);
            projection.append("_id", 0); 

            Document result = collection.find(filter).first();
            Document resultado=new Document();
            if (result != null) {
                // Obtener el subdocumento
                Document subdocument = (Document) result.get("propiedades");

                if (subdocument != null) {
                    // Imprimir los campos del subdocumento
                    for (String key : subdocument.keySet()) {
                        Object value = subdocument.get(key);
                        resultado.append(key, value);
                    }
                } else {
                    System.out.println("El subdocumento no está presente en el documento.");
                }
            } else {
                System.out.println("Documento no encontrado");
            }
    
            return resultado;

        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public static void modify(Document documentoViejo,Document documentoNuevo) {
		try {
            MongoClient mongoClient = MongoDB.getClient();
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");
            Document update = new Document("$set", documentoNuevo);
            UpdateResult result = collection.updateOne(documentoViejo, update);
            
            if (result.getModifiedCount() > 0) {
                System.out.println("Documento modificado correctamente en MongoDB.");
            } else {
                System.out.println("No se pudo modificar");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
