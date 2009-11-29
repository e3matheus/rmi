import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.*;
import java.io.*;

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


  public static boolean comPorArchivo(Msg c, BufferedReader br) {
    String fromFile = "";
    System.out.println("Ingresando comandos del archivo: ");
    try {
      while ((fromFile =  br.readLine()) != null) {
        if (verificarComando(fromFile)) {
          System.out.println(fromFile);
          if (fromFile.equals("q")) {
            System.out.println("Saliendo del sistema");
            usuThread.quit = true;
            return true;
          } else {
            getResponse(c, fromFile);
          }
        } else {
          System.out.println("ERROR: '" + fromFile + "' no es un comando valido!");
        }
      }
    } catch (IOException e) {
      System.out.println("Excepcion IOException en br.readln()");
      System.out.println(e);
    }

    return false;
  }

  //Envia mensajes del servidor.
  static class PrintThread extends Thread {
    private String aU;
    private Msg c;

    PrintThread(String aliasUsu, Msg c) {
      this.aU = aliasUsu;
      this.c = c;
    }

    public void run() { 
      boolean quit = false;
      try {
        while( ! quit ) {
          if (Msg.men.containsKey(aU)) {
            System.out.println(c.enviarMensajes(aU));
          }
        }
      } catch (IOException e) {
        System.out.println("Excepcion E/S en la construccion del buffer de entrada o el de salida del socket del cliente: " + e);
      }
    }
  }

  //Imprime Mensajes enviados del servidor.
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

  public static void comPorConsola(Msg c) {
    String fromUser = "";
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Ingrese comandos: ");
    try {
      while (!(fromUser =  br.readLine()).equals("q")) {
        if (fromUser != null) {
          if (verificarComando(fromUser)) {
            System.out.println(fromUser);
            getResponse(c, fromUser);
          } else {System.out.println("ERROR: '" + fromUser + "' no es un comando valido!");}
        }
      }
      System.out.println("Saliendo del sistema");
      // usuThread.quit = true;
      //out.println(fromUser);
    }
    catch (IOException e) {
      System.out.println("Excepcion IOException en br.readln()");
      System.out.println(e);
    }
  }

  public static void getResponse(Msg c, String clientRequest) { 
    String loginUsu = "", aliasUsu = "", mensaje = "";
    boolean autenticado = false;

    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(isr);

      if (clientRequest.startsWith("q")) {
        if (autenticado)
          Msg.conectados.remove(aliasUsu);
      } else 
        if (!(autenticado) && !(clientRequest.startsWith("l"))) {
          System.out.println("ERROR: Necesita autenticarse en el sistema para emplear este comando!");
        } else 
          if (autenticado && clientRequest.startsWith("l") || clientRequest.startsWith("c")) {
            System.out.println("Usted ya se encuentra autenticado en el sistema");
          } else 
            if (clientRequest.startsWith ("l")) {
              if (Msg.usuarios.containsKey(clientRequest.substring(2))){
                System.out.println("Emplee el comando para ingresar la clave");
                String clave = in.readLine();
                if (clave.startsWith ("c")) {
                  //autenticado = autenticarUsuario(usuarios, clientRequest.substring(2), clave.substring(2), out);
                  autenticado = true;
                  if (autenticado) {
                    loginUsu = clientRequest.substring(2);
                    infoUsuario iu = (infoUsuario) Msg.usuarios.get(loginUsu);
                    aliasUsu = iu.getAlias();
                    Msg.conectados.add(aliasUsu);
                    Thread hilo = new PrintThread(aliasUsu, c);
                    hilo.start();
                  }
                } else {
                  System.out.println("Una vez ingresado el login debe ingresar la clave para ser autenticado, ingrese de nuevo su login.");
                }
              } else {
                System.out.println("login no registrado en el sistema");
              }
            } else 
              if (clientRequest.startsWith ("c")) {
                System.out.println("Primero debe ingresar el login para poder asociarlo a su clave");
              } else 
                if (autenticado && clientRequest.equals("u")) {
                  System.out.println(c.getUsuarios());
                } else
                  if (autenticado && clientRequest.startsWith("s")) {
                    System.out.println(c.agregarSuscriptor(aliasUsu, clientRequest.substring(2)));
                  } else
                    if (autenticado && clientRequest.startsWith("d")) {		
                      System.out.println(c.eliminarSuscriptor(aliasUsu,clientRequest.substring(2)));
                    } else
                      if (autenticado && clientRequest.startsWith("m")) {
                        if (clientRequest.substring(2).startsWith("#")) {
                          mensaje = "Alias " + aliasUsu + ":" + clientRequest.substring(3);
                          //getSuscriptores(loginUsu);                              
                          infoUsuario u = (infoUsuario) Msg.usuarios.get(loginUsu);
                          LinkedList l = u.getSuscriptores();
                          Iterator itr = l.iterator();
                          while (itr.hasNext()) 
                            c.agregarMensaje((String) itr.next(), mensaje);
                        }
                        else { 
                          StringTokenizer t = new StringTokenizer(clientRequest.substring(2),"#");  
                          String usu = t.nextToken();
                          mensaje = "Alias " + aliasUsu + ":" + t.nextToken();
                          c.agregarMensaje(usu, mensaje);
                        }
                        System.out.println("Mensaje enviado");
                      }
    } catch (java.io.InterruptedIOException ie) {
      //gpt.interrupt();
    } catch (IOException e) {
      System.out.println("Excepcion E/S en la construccion del buffer de entrada o el de salida del socket del cliente: " + e);
    }
  }

  public static void main(String[] args) {
    //  Este programa asume que el rmiregistry estara en la maquina
    //    akarso y recibira peticiones por el puerto 21000
    try {
      BufferedReader br = null;
      int port = 0;
      String host = "";
      boolean a = false;

      // Asignacion de Parametros.
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

      Msg c = (Msg) Naming.lookup("rmi://" + host + ":" + port + "/MsgService"); 


      // Imprime los mensajes de este usuario, que recibe del servidor. 
      //       Thread t = new usuThread(in);
      //        t.start();

      if (!a)
        comPorConsola(c);
      else if (!comPorArchivo(c, br))
        comPorConsola(c);

      //t.interrupt();
    }
    catch (MalformedURLException murle) {
      System.out.println();
      System.out.println(
          "MalformedURLException");
      System.out.println(murle);
    }
    catch (RemoteException re) {
      System.out.println();
      System.out.println(
          "RemoteException");
      System.out.println(re);
    }
    catch (NotBoundException nbe) {
      System.out.println();
      System.out.println(
          "NotBoundException");
      System.out.println(nbe);
    }

  }
}

