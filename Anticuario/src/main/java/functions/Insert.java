package functions;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dao.MongoDB;

public class Insert {
	public static void addToMongo(Document document) {
		try (MongoClient mongoClient = MongoDB.getClient()) {
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");
            collection.insertOne(document);

            System.out.println("Documentos insertados correctamente en MongoDB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
