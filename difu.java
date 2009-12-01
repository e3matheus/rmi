import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.io.*;
import java.util.*;

public class difu {

  public difu(String usuarios, int port) {
    try {
      //  Usar la linea de abajo para no hacer la llamada a rmiregistry
      //     desde la linea de comandos, de esta forma el mismo programa 
      //     servidor crea su propio servicio de nombres antes de publicar 
      //     el objeto remoto c. 
      LocateRegistry.createRegistry(port);
      Msg c = new MsgImpl(usuarios);
      //  Este programa asume que el rmiregistry estara activo en la maquina
      //     akarso, y en el puerto 21000
      Naming.rebind("rmi://akarso.ldc.usb.ve:" + port + "/MsgService", c);


    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

	public static void main(String args[]) {
		int port = 0;
		String cuentas = "";
		Hashtable<String, LinkedList<String>> mensajes = new Hashtable<String, LinkedList<String>>();
		LinkedList<String> conectados = new LinkedList<String>();

		try {
			if (args.length == 4) {
				for (int i=0; i < (args.length - 1); i = i+2) {
					if (args[i].equals("-l")){
						port = Integer.parseInt(args[i+1]);
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

				new difu(cuentas, port);
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(isr);
        String fromS;
        boolean quit = false;
        while( ! quit ) {
          if ((fromS = in.readLine()) != null){
            if (fromS.equals("i") || fromS.equals("f"))  
            	MsgImpl.i = fromS;
						else if (fromS.equals("q"))
							System.exit(1);
          }
        }
				System.out.println("Hasta luego.");
				in.close();
				
      } catch (java.io.InterruptedIOException ie) {
      } catch (IOException e) {
        System.out.println("Excepcion IOException en in.readln()");
        System.out.println(e);
      }
  }
}
