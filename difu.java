import java.util.*;
import java.io.*;
import java.net.*;
import net.n3.nanoxml.*;

public class difu {

  static String getUsuarios(Hashtable<String, infoUsuario> u) {
    String aliases = "Usuarios del sistema: \n ", str;
    Set<String> set = u.keySet();
    Iterator<String> itr = set.iterator();
    while (itr.hasNext()) {
      str = itr.next();
      infoUsuario iu = (infoUsuario) u.get(str);
      aliases = aliases + " *" + iu.getAlias();
    }
    return aliases;
  }

  static String agregarSuscriptor(Hashtable<String, infoUsuario> u, String usu, String susc){
    String str;
    Set<String> set = u.keySet();
    Iterator<String> itr = set.iterator();
    while (itr.hasNext()) {
      str = itr.next();
      infoUsuario iu = (infoUsuario) u.get(str);
      if (iu.getAlias().equals(susc)) {
        if (iu.agregarS(usu))
          return "Se ha suscrito a los mensajes del usuario '" + susc + "' exitosamente";
        else return "No se encuentra el usuario especificado o ya se habia suscrito a el anteriormente";
      }
    }
    return "No se encuentra el usuario especificado o ya se habia suscrito a el anteriormente";
  }

  static String eliminarSuscriptor(Hashtable<String, infoUsuario> u, String usu, String susc){
    String str;
    Set<String> set = u.keySet();
    Iterator<String> itr = set.iterator();
    while (itr.hasNext()) {
      str = itr.next();
      infoUsuario iu = (infoUsuario) u.get(str);
      if (iu.getAlias().equals(susc)) {
        if (iu.eliminarS(usu))
          return "Se ha desuscrito de los mensajes del usuario '" + susc + "' exitosamente";
        else return "No se encuentra el usuario especificado o ya se habia desuscrito de el anteriormente";
      }
    }
    return "No se encuentra el usuario especificado o ya se habia desuscrito de el anteriormente";
  }

  static boolean autenticarUsuario(Hashtable<String, infoUsuario> u, String login, String clave, PrintWriter out) {
    infoUsuario inf = (infoUsuario) u.get(login);
    String claveUsu = inf.getClave();
    if (claveUsu.equals(clave)) {
      out.println("Ha sido autenticado exitosamente en el sistema");
      return true;
    } else {
      out.println("login o clave incorrecta!");
      return false;
    }
  }

  static void agregarMensaje(Hashtable<String, LinkedList<String>> tablam, String usu, String mensaje, LinkedList<String> c) {
    LinkedList<String> lm = new LinkedList<String>();
    if (!c.contains(usu))
      mensaje = mensaje + " " + new Date();
    if(tablam.containsKey(usu)){
      lm = (LinkedList<String>) tablam.get(usu);
      lm.add(mensaje);
    }
    else {
      lm.add(mensaje);
      tablam.put(usu,lm);
    }
  }

  static void enviarMensajes(Hashtable<String, LinkedList<String>> men, String aliasUsu, PrintWriter out) {
    LinkedList<String> lm = (LinkedList<String>) men.get(aliasUsu);
    out.println("m");
    out.println("Mensaje(s) recibido(s):");
    for (int i = lm.size(); i > 0; i--){
      getLine(lm.removeFirst(), out);
    }
    out.println("f");
    men.remove(aliasUsu);
  }

  static class difuThread extends Thread {
    public int i;

    difuThread() {
      super("Thread");
      i =1;
    }

    public void run() { 
      boolean quit = false;
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(isr);

      try {
        String fromS;
        while( ! quit ) {
          if ((fromS = in.readLine()) != null){
            MultiServerThread.i = fromS;
          }
        }
      } catch (java.io.InterruptedIOException ie) {
      } catch (IOException e) {
        System.out.println("Excepcion IOException en in.readln()");
        System.out.println(e);
      }
    }
  }

  static class PrintThread extends Thread {
    private Socket clientSocket = null;
    private Hashtable<String, LinkedList<String>> men = new Hashtable<String, LinkedList<String>>();
    private String aU;

    PrintThread(Socket clientSocket, String aliasUsu, Hashtable<String, LinkedList<String>> men) {
      super("Thread");
      this.clientSocket = clientSocket;
      this.men = men;
      this.aU = aliasUsu;
    }

    PrintThread(ThreadGroup group, Socket clientSocket, String aliasUsu, Hashtable<String, LinkedList<String>> men) {
      super(group, "Thread");
      this.clientSocket = clientSocket;
      this.men = men;
      this.aU = aliasUsu;
    }

    public void run() { 
      PrintWriter out = null;
      boolean quit = false;

      try {
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        while( ! quit ) {
          if (men.containsKey(aU)) {
            enviarMensajes(men, aU, out);
          }
        }

      } catch (java.io.InterruptedIOException ie) {
        System.out.println("Muere printthread..");
      } catch (IOException e) {
        System.out.println("Excepcion E/S en la construccion del buffer de entrada o el de salida del socket del cliente: " + e);
      }

    }
  }

  static public void getLine(String msg, PrintWriter out){
    out.println(msg);
    if (MultiServerThread.i.equals("i")) {
      System.out.println(msg);
    } else if  (MultiServerThread.i.equals("f")) {

    } else if  (MultiServerThread.i.equals("q")) {
      Thread.currentThread().interrupt();
    } else if  (MultiServerThread.i.equals("")) {
    } else {
      System.out.println("Comando no valido");
    }
  }

  static class MultiServerThread extends Thread {
    private Socket clientSocket = null;
    private Hashtable<String, infoUsuario> usuarios = new Hashtable<String, infoUsuario>();
    private Hashtable<String, LinkedList<String>> men = new Hashtable<String, LinkedList<String>>();
    private LinkedList<String> conectados = new LinkedList<String>();
    public static String i = "";

    MultiServerThread(Socket clientSocket, Hashtable<String, infoUsuario> usuarios, Hashtable<String, LinkedList<String>> men, LinkedList<String> conectados) {
      super("MultiServerThread");
      this.clientSocket = clientSocket;
      this.usuarios = usuarios;
      this.men = men;
      this.conectados = conectados;
    }

    MultiServerThread(ThreadGroup group, String sm, Socket clientSocket, Hashtable<String, infoUsuario> usuarios, Hashtable<String, LinkedList<String>> men, LinkedList<String> conectados) {
      super(group, sm);
      this.clientSocket = clientSocket;
      this.usuarios = usuarios;
      this.men = men;
      this.conectados = conectados;
    }


    public void run() { 
      BufferedReader in = null;
      PrintWriter out = null;
      String clientRequest, loginUsu = "", aliasUsu = "", mensaje = "";
      boolean quit = false, autenticado = false;

      ThreadGroup gpt = new ThreadGroup("gpt");

      try {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        while( ! quit ) {
          clientRequest = in.readLine();
          if (clientRequest.startsWith("q")) {
            if (autenticado)
              conectados.remove(aliasUsu);
            quit = true;
          } else 
            if (!(autenticado) && !(clientRequest.startsWith("l"))) {
              out.println("ERROR: Necesita autenticarse en el sistema para emplear este comando!");
            } else 
              if (autenticado && clientRequest.startsWith("l") || clientRequest.startsWith("c")) {
                out.println("Usted ya se encuentra autenticado en el sistema");
              } else 
                if (clientRequest.startsWith ("l")) {
                  if (usuarios.containsKey(clientRequest.substring(2))){
                    out.println("Emplee el comando para ingresar la clave");
                    String clave = in.readLine();
                    if (clave.startsWith ("c")) {
                      autenticado = autenticarUsuario(usuarios, clientRequest.substring(2), clave.substring(2), out);
                      if (autenticado) {
                        loginUsu = clientRequest.substring(2);
                        infoUsuario iu = (infoUsuario) usuarios.get(loginUsu);
                        aliasUsu = iu.getAlias();
                        conectados.add(aliasUsu);
                        Thread hilo = new PrintThread(gpt, clientSocket, aliasUsu, men);
                        hilo.start();
                      }
                    } else {
                      out.println("Una vez ingresado el login debe ingresar la clave para ser autenticado, ingrese de nuevo su login.");
                    }
                  } else {
                    out.println("login no registrado en el sistema");
                  }
                } else 
                  if (clientRequest.startsWith ("c")) {
                    out.println("Primero debe ingresar el login para poder asociarlo a su clave");
                  } else 
                    if (autenticado && clientRequest.equals("u")) {
                      out.println(getUsuarios(usuarios));
                    } else
                      if (autenticado && clientRequest.startsWith("s")) {
                        out.println(agregarSuscriptor(usuarios, aliasUsu, clientRequest.substring(2)));
                      } else
                        if (autenticado && clientRequest.startsWith("d")) {		
                          out.println(eliminarSuscriptor(usuarios,aliasUsu,clientRequest.substring(2)));
                        } else
                          if (autenticado && clientRequest.startsWith("m")) {
                            if (clientRequest.substring(2).startsWith("#")) {
                              mensaje = "Alias " + aliasUsu + ":" + clientRequest.substring(3);
                              infoUsuario u = (infoUsuario) usuarios.get(loginUsu);
                              LinkedList<String> l = u.getSuscriptores();
                              Iterator<String> itr = l.iterator();
                              while (itr.hasNext()) {
                                agregarMensaje(men, itr.next(), mensaje, conectados);
                              }
                            }
                            else { 
                              StringTokenizer t = new StringTokenizer(clientRequest.substring(2),"#");  
                              String usu = t.nextToken();
                              mensaje = "Alias " + aliasUsu + ":" + t.nextToken();
                              agregarMensaje(men, usu, mensaje, conectados);
                            }
                            out.println("Mensaje enviado");
                          }
        }
      } catch (java.io.InterruptedIOException ie) {
        System.out.println("Muere multiserverThread" + getName());
        gpt.interrupt();
        System.out.println("Muere printThread??");
        try { in.close(); out.close(); clientSocket.close(); } catch (IOException e) {}
      } catch (IOException e) {
        System.out.println("Excepcion E/S en la construccion del buffer de entrada o el de salida del socket del cliente: " + e);
      }
    }
  }

  public static Hashtable<String, infoUsuario> parseaXML(String c) throws Exception {
    String login, clave, alias;
    IXMLElement child;
    IXMLParser parser = XMLParserFactory.createDefaultXMLParser(); 
    IXMLReader reader = StdXMLReader.fileReader(c); 
    parser.setReader(reader); 
    IXMLElement xml = (IXMLElement) parser.parse();

    Hashtable<String, infoUsuario> hash = new Hashtable<String, infoUsuario>();
    Enumeration <IXMLElement> enume = (Enumeration<IXMLElement>) xml.enumerateChildren();
    while (enume.hasMoreElements())
    {
      child = enume.nextElement();
      login = child.getChildAtIndex(0).getContent();
      clave = child.getChildAtIndex(1).getContent();
      alias = child.getChildAtIndex(2).getContent();
      infoUsuario us1 = new infoUsuario(clave, alias); 
      hash.put(login, us1);
    }

    return hash;
  }

  public static void main(String[] args) {

    ServerSocket server;
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
      Hashtable<String, infoUsuario> usuarios = parseaXML(cuentas);
      server = new ServerSocket(port);
      System.out.println("Servidor difu activado" );
      int i = 0;
      String s = "" + i;
      ThreadGroup G = new ThreadGroup("grupo");
      boolean quit = false;

      //Actualiza el valor de la variable estatica del multiserverthread 
      Thread t = new difuThread();
      t.start();

      t.interrupt();

      while (!quit){
        new MultiServerThread(G, s, server.accept(), usuarios, mensajes, conectados).start();
        if (i ==1){
          System.out.println("Interrumpo a todos...");
          G.interrupt();
          quit = true;
        }
        i = i + 1;
        s = s + i;
      }

    } catch (IOException e) {
      System.out.println("Excepcion E/S en el constructor del servidor: " + e);
    } catch (Exception e) {
      System.out.println("Excepcion de E/S: Error parseando el archivo");
      System.out.println(e);
    } 
  } 
} 
