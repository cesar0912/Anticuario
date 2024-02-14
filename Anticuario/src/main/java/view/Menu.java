package view;

import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;

import dao.MongoDB;
import functions.Delete;
import functions.Find;
import functions.Insert;
import functions.Modify;
import io.IO;

public class Menu {
	public static void main(String[] args) {

		List<String> opciones = List.of("1: Añadir producto", "2: Buscar producto", "3: Modificar producto",
				"4: Eliminar producto");
		MongoDB.getClient();
		while (true) {
			System.out.println(opciones);
			switch (IO.readInt()) {
			case 1:add();
				break;
			case 2: find();
				break;
			case 3:modify();
				break;
			case 4:delete();
				break;
			default:
			}
		}

	}

	public static void add() {
		Document document=new Document();
		Document subDocument=new Document();
		
		// Datos generales
		System.out.println("Introduzca el nombre del producto");
		String datoNombre=IO.readString();
		System.out.println("Introduzca su tipo");
		String datoTipo=IO.readString();
		System.out.println("Introduzca su precio");
		String datoPrecio=IO.readString();

		
		// Datos suplementarios
		//TODO Cambiar for por while
		System.out.println("¿Cuantos datos quiere añadir?");
		int cant = IO.readInt();
		if (cant > 0) {
		System.out.println("Insertelo en este formato(tipo:dato)");
		for(int i=0;i<cant;i++) {
			String datos[]=IO.readString().split(":");
			subDocument.append(datos[0],datos[1]);
		}
		
		
			document.append("nombre", datoNombre).append("tipo", datoTipo).append("precio", datoPrecio).append("propiedades", subDocument);
		} else {
			document.append("nombre", datoNombre).append("tipo", datoTipo).append("precio", datoPrecio);
		}
		
		
		Insert.addToMongo(document);
	}

	public static void find() {
		
		System.out.println("1: Buscar por dato  2: Buscar todo");
		
		switch (IO.readInt()) {
			case 1:System.out.println("Pon el dato por el que quieres filtrar");
				String dato=IO.readString();
				Find.FindField(dato);
				break;
				
			case 2:
				Find.FindAll();
				break;
			
		}

	}

	public static void modify() {

		Document document=new Document();
		Document subDocument=new Document();
		System.out.println("Introduzca el id del producto a modificar");
		String id = IO.readString();
		Document found=Modify.findById(id);
		
		if (found!=null) {
			System.out.println(Find.pretty(found.toJson()));
            Document encontrado=Modify.modifyById(id);
            Set<String> campos = encontrado.keySet();
            System.out.println("Campos del documento a modificar:");
            String dato;
            System.out.println("Introduzca el nombre del producto");
    		String datoNombre=IO.readString();
    		System.out.println("Introduzca su tipo");
    		String datoTipo=IO.readString();
    		System.out.println("Introduzca su precio");
    		String datoPrecio=IO.readString();
            for (String campo : campos) {
            	
            	System.out.println("Inserte "+campo);
        		dato=IO.readString();
        		subDocument.append(campo,dato);
        			
        			
            }
            if(campos!=null)
            document.append("nombre", datoNombre).append("tipo", datoTipo).append("precio", datoPrecio).append("propiedades", subDocument);
            else
            document.append("nombre", datoNombre).append("tipo", datoTipo).append("precio", datoPrecio);
            Modify.modify(found,document);
        } else {
            System.out.println("Documento no encontrado.");
        }
	}

	public static void delete() {
		Document document = new Document();
		Document subDocument = new Document();
		
		System.out.println("1: Borrar un artículo concreto  2: Borrar por datos  3: Borrar por valor del dato");
		
		switch (IO.readInt()) {
			case 1:
				System.out.println("Introduzca el id del producto a borrar");
				String id = IO.readString();
				
				Delete.deleteByID(id);
				break;
			
			case 2: 
				System.out.println("Introduzca la clasificacion que quiere borrar");
				String dato = IO.readString();
				
				Delete.deleteByKey(dato);
				break;
				
			case 3:
				System.out.println("Cuántos datos quieres introducir para filtrar el borrado");
				int cant = IO.readInt();

				System.out.println("Formato(tipo:dato)");
				for(int i=0;i<cant;i++) {
					
					String datos[]=IO.readString().split(":");
					switch(datos[0]) {
					case "nombre":document.append(datos[0],datos[1]);
						break;
					case "tipo":document.append(datos[0],datos[1]);
						break;
					case "precio":document.append(datos[0],datos[1]);
						break;
					default:
						subDocument.append(datos[0],datos[1]);
						break;
						
					}
					
				}
				document.append("propiedades", subDocument);
				Delete.deleteByValues(document);
				break;
	
			default:
		}
		
		
	}
}
