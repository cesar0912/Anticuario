package functions;

import java.util.Iterator;
import java.util.Set;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import dao.MongoDB;

public class Find {
	public static void FindField(String dato) {
		try {
            MongoClient mongoClient = MongoDB.getClient();
            MongoDatabase database = mongoClient.getDatabase("Anticuario");
            MongoCollection<Document> collection = database.getCollection("Productos");

            FindIterable<Document> iterable;
            Iterator<Document> iterator;
            if (dato.contains(":")) {
                String[] datos = dato.split(":");
                String campo = datos[0];
                String valor = datos[1];
                if(!campo.equals("nombre") && !campo.equals("tipo") && !campo.equals("precio")) {
                	iterable = collection.find(Filters.eq("propiedades."+campo, valor));
                }else {
                	iterable = collection.find(Filters.eq(campo, valor));
                }
	            iterator = iterable.iterator();
                while (iterator.hasNext()) {
	                Document document = iterator.next();
                    System.out.println(pretty(document.toJson()));
                }
            } else {
            	iterable = collection.find();
            	iterator = iterable.iterator();
            	boolean escrito=false;
            	while (iterator.hasNext()) {
            		escrito=false;
            	    Document document = iterator.next();
            	    // Verificar si el documento contiene un subdocumento "propiedades"
            	    if (document.containsKey("propiedades")) {
            	        Document propiedades = document.get("propiedades", Document.class);
            	        // Iterar sobre las claves del subdocumento "propiedades"
            	        Set<String> propiedadesKeys = propiedades.keySet();
            	        for (String propKey : propiedadesKeys) {
            	            Object propValue = propiedades.get(propKey);
            	            // Verificar si el valor es una cadena y si contiene el dato buscado
            	            if (propValue instanceof String) {
            	                String propStringValue = (String) propValue;
            	                if (propStringValue.equals(dato) || propKey.equals(dato)) {
            	                    System.out.println(pretty(document.toJson()));
            	                    escrito=true;
            	                    break; // Romper el bucle interno si se encuentra una coincidencia
            	                }
            	            }
            	        }
            	    }
            	    if(!escrito) {
	            	    Set<String> keys = document.keySet();
	            	    for (String key : keys) {
	            	        Object value = document.get(key);
	            	        if (value instanceof String) {
	            	            String stringValue = (String) value;
	            	            if (stringValue.equals(dato) || key.equals(dato)) {
	            	                System.out.println(pretty(document.toJson()));
	            	                break; // Romper el bucle interno si se encuentra una coincidencia
	            	            }
	            	        }
	            	    }
            	    }
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

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
