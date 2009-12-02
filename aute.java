import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.io.*;
import net.n3.nanoxml.*;

public class aute {
	
	public aute(String port, Hashtable<String, String[]> lc) {
		try {
			LocateRegistry.createRegistry(Integer.parseInt(port));
			Autenticador a = new AutenticadorImpl(lc);
			Naming.rebind("rmi://localhost:"+port+"/auteService", a);
		}
		catch (Exception e) {
			System.out.println ("Error: " + e);
		}
	}
	
	public static Hashtable<String, String[]> parseaXML(String c) throws Exception {
		String login, clave, alias;
		IXMLElement child;
		IXMLParser parser = XMLParserFactory.createDefaultXMLParser(); 
		IXMLReader reader = StdXMLReader.fileReader(c); 
		parser.setReader(reader); 
		IXMLElement xml = (IXMLElement) parser.parse();
		Hashtable<String, String[]> hash = new Hashtable<String, String[]>();
		Enumeration<IXMLElement> enume = (Enumeration<IXMLElement>) xml.enumerateChildren();
		while (enume.hasMoreElements()) {
		  String[] us1 = new String[2];
			child = enume.nextElement();
			login = child.getChildAtIndex(0).getContent();
			clave = child.getChildAtIndex(1).getContent();
			alias = child.getChildAtIndex(2).getContent();
			us1[0] = clave;
			us1[1] = alias;
			System.out.println(login+" "+us1[0]+" "+us1[1]);
			hash.put(login, us1);
		}

			System.out.println("fin");
		return hash;
	}
  
	public static void main(String args[]) {
		String port = "", cuentas = "";
		
		try {
			if (args.length == 4) {
				for (int i=0; i < (args.length - 1); i = i+2) {
					if (args[i].equals("-l")){
						port = args[i+1];
					} else 
						if (args[i].equals("-a")){
							cuentas = args[i+1];
						} else {
                          System.out.println("Ingreso incorrecto de parametros!");
						}
				}
			} else {
				System.out.println("Numero de parametros incorrecto!");
				return;
			}
		}
		catch (Exception e) {
			System.out.println("Excepcion de E/S: El numero de parametros ingresados es incorrecto");
			System.out.println(e);
		}

		try {
		Hashtable<String, String[]> lc = parseaXML(cuentas);
		new aute(port, lc);
		} catch (Exception e) {
			System.out.println("Error al crear la estructura de la informacion de las cuentas de los usuarios");
		}
	}
}
