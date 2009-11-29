import java.util.*;

public class MsgImpl extends java.rmi.server.UnicastRemoteObject implements Msg {
  static final long serialVersionUID = 0;
  public Hashtable usuarios = new Hashtable();
  public Hashtable men = new Hashtable();
  public LinkedList conectados = new LinkedList();

  // Implementations must have an 
  //explicit constructor
  // in order to declare the 
  //RemoteException exception
  public MsgImpl()
    throws java.rmi.RemoteException {
      super();
    }

  public String enviarMensajes(String aliasUsu) throws java.rmi.RemoteException{
    String ret;
    LinkedList lm = (LinkedList) men.get(aliasUsu);
    ret = "m";
    ret += "Mensaje(s) recibido(s):";
    for (int i = lm.size(); i > 0; i--){
      ret += lm.removeFirst();
    }
    ret += "f";
    men.remove(aliasUsu);
    return ret;
  }

  public void agregarMensaje(String usu, String mensaje) throws java.rmi.RemoteException{
    LinkedList lm = new LinkedList();
    if (!conectados.contains(usu))
      mensaje = mensaje + " " + new Date();
    if(men.containsKey(usu)){
      lm = (LinkedList) men.get(usu);
      lm.add(mensaje);
    }
    else {
      lm.add(mensaje);
      men.put(usu,lm);
    }
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

}


