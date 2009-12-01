import java.util.*;

public class MsgImpl extends java.rmi.server.UnicastRemoteObject implements Msg {
  static final long serialVersionUID = 0;
  public Hashtable<String, infoUsuario> usuarios = new Hashtable<String, infoUsuario>();
  public static String i = "";
  public Autenticador a;

  // Implementations must have an 
  //explicit constructor
  // in order to declare the 
  //RemoteException exception
  public MsgImpl(Autenticador a)
    throws java.rmi.RemoteException {
      super();
      try {
        LinkedList<String> aliases = a.getAliases();
        Iterator<String> itr = aliases.iterator();
        String str = "";
        while (itr.hasNext()) {
          str = itr.next();
          this.usuarios.put(str, new infoUsuario());
        }

        this.a = a;
      } catch (Exception e) {
      }
    }

    static public void getLine(String msg){
    if (i.equals("i")) {
      System.out.println(msg);
    } else if (i.equals("f")) {
    } else if (i.equals("q")) {
    } else if (i.equals("")) {
    } else {
      System.out.println("Comando no valido");
    }
  }

  public String autenticarUsuario(String login, String clave)throws java.rmi.RemoteException {
    return a.autenticarUsuario(login, clave);
  }


  public String enviarMensajes(String aliasUsu) throws java.rmi.RemoteException{
    infoUsuario iu = usuarios.get(aliasUsu);
    String ret = "";

    String msj = iu.obtenerM();
    if (msj != null) {
      ret = "Mensaje(s) recibido(s):";
      while (msj != null){
        ret += msj;
        msj = iu.obtenerM();
      }
    }

    getLine(ret);
    return ret;
  }

  public void agregarMensaje(String usu, String mensaje) throws java.rmi.RemoteException{
    if (usu == null)
      System.out.println("usu es nulo chamo..");

    infoUsuario inf = usuarios.get(usu);

    if (!inf.getAutenticado())
      mensaje = mensaje + " " + new Date();
    inf.agregarM(mensaje);
  }

  public void agregarMensajeMultiple(String aliasUsu, String clientRequest) throws java.rmi.RemoteException{
    infoUsuario iu = usuarios.get(aliasUsu);
    String mensaje = "Alias " + aliasUsu + ":" + clientRequest.substring(3);
    LinkedList l = iu.getSuscriptores();
    Iterator itr = l.iterator();
    while (itr.hasNext()) 
      agregarMensaje((String) itr.next(), mensaje);
  }

  public String eliminarSuscriptor(String usu, String susc) throws java.rmi.RemoteException{
    String str;
    Set set = usuarios.keySet();
    Iterator itr = set.iterator();
    while (itr.hasNext()) {
      str = (String) itr.next();
      if (str.equals(susc)) {
        infoUsuario iu = usuarios.get(str);
        if (iu.eliminarS(usu))
          return "Se ha desuscrito de los mensajes del usuario '" + susc + "' exitosamente";
        else return "No se encuentra el usuario especificado o ya se habia desuscrito de el anteriormente";
      }
    }
    return "No se encuentra el usuario especificado o ya se habia desuscrito de el anteriormente";
  }

  public String agregarSuscriptor(String usu, String susc)throws java.rmi.RemoteException{
    Set set = usuarios.keySet();
    Iterator itr = set.iterator();
    String str;
    while (itr.hasNext()) {
      str = (String) itr.next();
      if (str.equals(susc)) {
        infoUsuario iu = usuarios.get(str);
        if (iu.agregarS(usu))
          return "Se ha suscrito a los mensajes del usuario '" + susc + "' exitosamente";
        else return "No se encuentra el usuario especificado o ya se habia suscrito a el anteriormente";
      }
    }
    return "No se encuentra el usuario especificado";
  }

  public String getUsuarios() throws java.rmi.RemoteException{
    String aliases = "Usuarios del sistema: \n ", str;
    Set set = usuarios.keySet();
    Iterator itr = set.iterator();
    while (itr.hasNext()) {
      aliases = aliases + " *" + itr.next();
    }
    return aliases;
  }

  public boolean containClave(String login)throws java.rmi.RemoteException{
    return usuarios.containsKey(login);
  } 

  public void desconectaUsu(String alias)throws java.rmi.RemoteException{
    infoUsuario iu = usuarios.get(alias);
    iu.setConectado(false);
    iu.setAutenticado(false);
  } 

  public void conectaUsu(String alias)throws java.rmi.RemoteException{
    infoUsuario iu = usuarios.get(alias);
    iu.setConectado(true);
    iu.setAutenticado(true);
  } 

   public boolean existenMensajes(String alias)throws java.rmi.RemoteException{
    infoUsuario iu = usuarios.get(alias);
    if (iu.mensajes.size() != 0){ 
      System.out.println(iu.mensajes.size());
      return true;
    }
    else   
      return false;
  }

 }
