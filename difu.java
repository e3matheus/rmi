import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.io.*;
import java.util.*;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

public class difu {

  public difu(Autenticador a, int port) {
    try {
      //  Usar la linea de abajo para no hacer la llamada a rmiregistry
      //     desde la linea de comandos, de esta forma el mismo programa 
      //     servidor crea su propio servicio de nombres antes de publicar 
      //     el objeto remoto c. 
      LocateRegistry.createRegistry(port);
      Msg c = new MsgImpl(a);
      //  Este programa asume que el rmiregistry estara activo en la maquina
      //     akarso, y en el puerto 21000
      Naming.rebind("rmi://localhost:" + port + "/MsgService", c);

      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(isr);
      boolean quit = false;
      String fromS;
      while( ! quit ) {
        if ((fromS = in.readLine()) != null){
          if (fromS.equals("i") || fromS.equals("f"))  
            MsgImpl.i = fromS;
          else if (fromS.equals("q"))
            System.exit(1);
          else if (fromS.equals("v"))
            if (c.pruebaAute())
              System.out.println("El servidor de autenticacion se encuentra disponible.");
            else
              System.out.println("El servidor no se encuentra disponible.");
        }
      }
      System.out.println("Hasta luego.");
      in.close();
    } catch (MalformedURLException nbe) {
    } catch (IOException e) {
      System.out.println("Excepcion IOException en in.readln()");
      System.out.println(e);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  public static void main(String args[]) {
    int difuPort = 0;
    String auteHost = "", autePort = "";
    boolean quit = false;
    Hashtable<String, infoUsuario> usuarios = new Hashtable<String, infoUsuario>();

    try {
      if (args.length == 6) {
        for (int i=0; i < (args.length - 1); i = i+2) {
          if (args[i].equals("-l")){
            difuPort = Integer.parseInt(args[i+1]);
          } else 
            if (args[i].equals("-p")){
              autePort = args[i+1];
            } else
              if (args[i].equals("-h")){
                auteHost = args[i+1];
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
      Autenticador a = (Autenticador) Naming.lookup("rmi://"+auteHost+":"+autePort+"/auteService");
      new difu(a, difuPort);

    } catch (MalformedURLException nbe) {
      System.out.println();
      System.out.println(
          "URL incorrecto");
      System.out.println(nbe);
    } catch (NotBoundException nbe) {
      System.out.println();
      System.out.println(
          "NotBoundException");
      System.out.println(nbe);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }

  }
}
