import java.util.*;

public interface Msg
          extends java.rmi.Remote {
  public Hashtable usuarios= new Hashtable();
  public Hashtable men = new Hashtable();
  public LinkedList conectados = new LinkedList();

    public String enviarMensajes(String aliasUsu) throws java.rmi.RemoteException;

    public void agregarMensaje(String usu, String mensaje) throws java.rmi.RemoteException;

    public String eliminarSuscriptor(String usu, String susc) throws java.rmi.RemoteException;

    public String agregarSuscriptor(String usu, String susc)throws java.rmi.RemoteException;

    public String getUsuarios() throws java.rmi.RemoteException;

}


