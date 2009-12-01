import java.util.*;
import net.n3.nanoxml.*;

public class MsgImpl extends java.rmi.server.UnicastRemoteObject implements Msg {
  static final long serialVersionUID = 0;

  //Login -> InfoUsuario
  public Hashtable usuarios = new Hashtable();
  // Alias -> Lista de Mensajes
  public Hashtable<String, LinkedList<String>> men = new Hashtable<String, LinkedList<String>>();
  // Alias
  public LinkedList<String> conectados = new LinkedList<String>();
  public static String i = "";

  // Implementations must have an 
  //explicit constructor
  // in order to declare the 
  //RemoteException exception
  public MsgImpl(String cuenta)
    throws java.rmi.RemoteException {
      super();
      try {
        this.usuarios = parseaXML(cuenta); 
        //System.out.println(usuarios.toString());
      } catch (Exception e) {
      }
    }

  public Hashtable<String, infoUsuario> parseaXML(String c) throws Exception {
    String login, clave, alias;
    IXMLElement child;
    IXMLParser parser = XMLParserFactory.createDefaultXMLParser(); 
    IXMLReader reader = StdXMLReader.fileReader(c); 
    parser.setReader(reader); 
    IXMLElement xml = (IXMLElement) parser.parse();

    //java.security.MessageDigest t_md5 = java.security.MessageDigest.getInstance("MD5");
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
      //   System.out.println("login = " + login + ", alias = " + alias + ", clave = " +clave);
    }

    return hash;
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

  public String enviarMensajes(String aliasUsu) throws java.rmi.RemoteException{
    String ret;
    LinkedList<String> lm = men.get(aliasUsu);

    //ret = "m";
    ret = "Mensaje(s) recibido(s):";
    for (int i = lm.size(); i > 0; i--){
      ret += lm.removeFirst();
    }
    
    getLine(ret);
    //ret += "f";
    men.remove(aliasUsu);
    return ret;
  }

  public void agregarMensaje(String usu, String mensaje) throws java.rmi.RemoteException{
    LinkedList<String> lm = new LinkedList<String>();
    if (usu == null)
      System.out.println("usu es nulo chamo..");
    if (!conectados.contains(usu))
      mensaje = mensaje + " " + new Date();
    if(men.containsKey(usu)){
      lm = men.get(usu);
      lm.add(mensaje);
    }
    else {
      lm.add(mensaje);
      men.put(usu,lm);
    }
  }

  public void agregarMensajeMultiple(String loginUsu, String clientRequest) throws java.rmi.RemoteException{
    infoUsuario iu = (infoUsuario) usuarios.get(loginUsu);
    String aliasUsu = iu.getAlias();
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
      infoUsuario iu = (infoUsuario) usuarios.get(str);
      if (iu.getAlias().equals(susc)) {
        if (iu.eliminarS(usu))
          return "Se ha desuscrito de los mensajes del usuario '" + susc + "' exitosamente";
        else return "No se encuentra el usuario especificado o ya se habia desuscrito de el anteriormente";
      }
    }
    return "No se encuentra el usuario especificado o ya se habia desuscrito de el anteriormente";
  }

  public String agregarSuscriptor(String usu, String susc)throws java.rmi.RemoteException{
    String str;
    Set set = usuarios.keySet();
    Iterator itr = set.iterator();
    while (itr.hasNext()) {
      str = (String) itr.next();
      infoUsuario iu = (infoUsuario) usuarios.get(str);
      if (iu.getAlias().equals(susc)) {
        if (iu.agregarS(usu))
          return "Se ha suscrito a los mensajes del usuario '" + susc + "' exitosamente";
        else return "No se encuentra el usuario especificado o ya se habia suscrito a el anteriormente";
      }
    }
    return "No se encuentra el usuario especificado o ya se habia suscrito a el anteriormente";
  }

  public String getUsuarios() throws java.rmi.RemoteException{
    String aliases = "Usuarios del sistema: \n ", str;
    Set set = usuarios.keySet();
    Iterator itr = set.iterator();
    while (itr.hasNext()) {
      str = (String) itr.next();
      infoUsuario iu = (infoUsuario) usuarios.get(str);
      aliases = aliases + " *" + iu.getAlias();
    }
    return aliases;
  }

  public boolean containClave(String login)throws java.rmi.RemoteException{
    return usuarios.containsKey(login);
  } 

  public boolean remueveConectados(String alias)throws java.rmi.RemoteException{
    return conectados.remove(alias);
  } 

  public void agregaConectados(String login) throws java.rmi.RemoteException{
    infoUsuario iu = (infoUsuario) usuarios.get(login);
    String aliasUsu = iu.getAlias();
    conectados.add(aliasUsu);
  } 

  public boolean existenMensajes(String alias)throws java.rmi.RemoteException{
    return men.containsKey(alias);
  }

  public boolean isAutenticado(String login) throws java.rmi.RemoteException{
    infoUsuario iu = (infoUsuario) usuarios.get(login);
    if (iu != null){
      String aliasUsu = iu.getAlias();
      return conectados.contains(aliasUsu);
    }
    else 
      return false;
  } 

  public String getAlias(String login) throws java.rmi.RemoteException {
    infoUsuario iu = (infoUsuario) usuarios.get(login);
    return iu.getAlias();
  } 

}
