import java.util.*;

public interface Msg
extends java.rmi.Remote {
  public static Hashtable usuarios = new Hashtable();
  public static Hashtable<String, LinkedList<String>> men = new Hashtable<String, LinkedList<String>>();
  public static LinkedList<String> conectados = new LinkedList<String>();

  public String enviarMensajes(String aliasUsu) throws java.rmi.RemoteException;

  public void agregarMensaje(String usu, String mensaje) throws java.rmi.RemoteException;

  public String eliminarSuscriptor(String usu, String susc) throws java.rmi.RemoteException;

  public String agregarSuscriptor(String usu, String susc)throws java.rmi.RemoteException;

  public String autenticarUsuario(String login, String clave)throws java.rmi.RemoteException ;

  public String getUsuarios() throws java.rmi.RemoteException;

  public boolean containClave(String s) throws java.rmi.RemoteException;

  public boolean isAutenticado(String alias)  throws java.rmi.RemoteException;

  public void desautentica(String alias) throws java.rmi.RemoteException;

  public void agregarMensajeMultiple(String loginUsu, String clientRequest) throws java.rmi.RemoteException;

  public void conectaUsu(String alias)throws java.rmi.RemoteException;

  public void desconectaUsu(String alias)throws java.rmi.RemoteException;

  public boolean existenMensajes(String alias)throws java.rmi.RemoteException;
}
