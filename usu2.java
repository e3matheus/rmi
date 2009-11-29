import java.util.*;
import java.io.*;
import java.net.*;

public class usu {

  public static boolean verificarComando(String comando){
    if (comando.equals("q") || comando.equals("u"))
      return true;
    else 
      if ((comando.startsWith ("l") || comando.startsWith("c") || comando.startsWith("s") || comando.startsWith("d")) && comando.length() > 2)
        return true;
      else 
        if ((comando.startsWith("m")) && comando.length() > 2) {
          String men = comando.substring(2);
          if (men.startsWith("#"))
            if (men.length() > 121) {
              System.out.println("El mensaje no debe tener mas de 120 caracteres!");
              return false;
            }
            else return true;
          else {
            try {
              StringTokenizer t = new StringTokenizer(men,"#");
              String m = t.nextToken();
              m = t.nextToken();
              if (m.length() > 120) {
                System.out.println("El mensaje no debe tener mas de 120 caracteres!");
                return false;
              }
            }
            catch (NoSuchElementException e) {
              System.out.println("Fomato del comando 'm' incorrecto!");
              return false;					
            }
            return true;
          }
        } else 
          return false;
  }

  public static boolean comPorArchivo(BufferedReader br, BufferedReader in, PrintWriter out) throws IOException {
    String fromFile = "", fromServer;
    System.out.println("Ingresando comandos del archivo: ");
    while ((fromFile = br.readLine()) != null) {
      if (verificarComando(fromFile)) {
        System.out.println(fromFile);
        if (fromFile.equals("q")) {
          System.out.println("Saliendo del sistema");
          out.println(fromFile);
          return true;
        } else {
          out.println(fromFile);	
          fromServer = in.readLine();
          if (fromServer.equals("m")) {
            while (!((fromServer = in.readLine()).equals("f"))) {
              System.out.println(fromServer);
            }
            fromServer = in.readLine();
          }				 
          if (fromFile.equals("u")) {
            System.out.println("Usuarios del sistema:\n" + fromServer);
          } else {
            System.out.println("El servidor responde: '"+ fromServer +"'");
          }
        }
      } else {System.out.println("'" + fromFile + "' no es un comando valido!");}
    }
    return false;
  }

  public static String recibeComandos(BufferedReader in, BufferedReader br){
    String fU = "";
    try
    {
      fU = br.readLine();
    } catch (IOException e) {
      System.out.println("Excepcion IOException en br.readln()");
      System.out.println(e);
    }
    return fU;
  }

    static class usuThread extends Thread {
    private BufferedReader in;
    static boolean quit;

    usuThread(BufferedReader in) {
      super("Thread");
      this.in = in;
    }

    public void run() { 
      quit = false;
      String fromS = "";

      try {
        while( ! quit ) {
          if ((fromS = in.readLine()) != null){
            if (fromS.equals("m")) {
              while (!((fromS = in.readLine()).equals("f"))) {
                System.out.println(fromS);
              }
            } else {
              System.out.println(fromS);
            } 
          }
        }
      } catch (java.io.InterruptedIOException ie) {

      } catch (IOException e) {
        System.out.println("Excepcion IOException en in.readln()");
        System.out.println(e);
      }
    }
  }

  public static void comPorConsola(BufferedReader in, PrintWriter out) {
    String fromUser = "";
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Ingrese comandos: ");
    while (!(fromUser = recibeComandos(in, br)).equals("q")) {
      if (fromUser != null) {
        if (verificarComando(fromUser)) {
          out.println(fromUser);
        } else {System.out.println("ERROR: '" + fromUser + "' no es un comando valido!");}
      }
    }
    System.out.println("Saliendo del sistema");
    usuThread.quit = true;
    out.println(fromUser);
    /*  }
        catch (IOException e) {
        System.out.println("Excepcion IOException en br.readln()");
        System.out.println(e);
        }*/
  }

  public static void main(String[] args) {
    Socket client;
    BufferedReader in = null, br = null;
    PrintWriter out = null;
    int port = 0;
    String host = "";
    boolean a = false;

    if (args.length == 4 || args.length == 6) {
      for (int i=0; i < (args.length -1); i=i+2) {
        if (args[i].equals("-h")) {
          host = args[i+1];
        } else 
          if (args[i].equals("-p")){
            port = Integer.parseInt(args[i+1]);
          } else 
            if (args[i].equals("-c")){
              try {
                br = new BufferedReader(new FileReader(args[i+1]));
              } 
              catch (FileNotFoundException e) {
                System.out.println("No se encuentra el archivo especificado!: " + e);
                return;
              }
              a = true;
            } else {
              System.out.println("Ingreso de parametros incorrecto!");
              return;
            }
      }
    } else {
      System.out.println("Numero de parametros incorrecto!");
      return;
    }

    try {
      client = new Socket(host, port);
      in = new BufferedReader(new InputStreamReader( client.getInputStream())); 
      out = new PrintWriter(client.getOutputStream(), true);
   
      // Imprime los mensajes que recibe del servidor. 
      Thread t = new usuThread(in);
      t.start();

      if (!a)
        comPorConsola(in, out);
      else if (!comPorArchivo(br, in, out))
        comPorConsola(in, out);
  
      t.interrupt();
      in.close();
      client.close();
      //Thread.sleep(500);

    } catch (IOException e) {
  }
} 

} 

