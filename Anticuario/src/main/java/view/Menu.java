package view;

import java.util.List;

import org.bson.Document;

import dao.MongoDB;
import functions.Insert;
import io.IO;

public class Menu {
	public static void main(String[] args) {

		List<String> opciones = List.of("1: Añadir producto", "2: Buscar producto", "3: Modificar producto",
				"4: Eliminar producto");

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
		System.out.println("tipo?");
		String dato=IO.readString();
		System.out.println("¿Cuantos datos quiere añadir?");
		int cant = IO.readInt();
		System.out.println("Insertelo en este formato(tipo:dato)");
		for(int i=0;i<cant;i++) {
			String datos[]=IO.readString().split(":");
			subDocument.append(datos[0],datos[1]);
		}
		document.append("tipo", dato).append("propiedades", subDocument);
		Insert.addToMongo(document);
		MongoDB.getClient();
	}

	public static void find() {

	}

	public static void modify() {

	}

	public static void delete() {

	}
}
