import java.util.*;

public interface Autenticador extends java.rmi.Remote {
	Hashtable<String, String[]> lc = new Hashtable<String, String[]>();
	static final int WAITING = 0;
    static final int SENTCHALLENGE = 1;
    int state = WAITING;
	
	public String autenticarUsuario(String l, String c) throws java.rmi.RemoteException;
	public LinkedList<String> getAliases() throws java.rmi.RemoteException;
}