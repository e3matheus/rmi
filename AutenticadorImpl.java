import java.util.*;

public class AutenticadorImpl extends java.rmi.server.UnicastRemoteObject
	implements Autenticador {
	
	Hashtable<String, String[]> lc = new Hashtable<String, String[]>();
	
	public AutenticadorImpl(Hashtable<String, String[]> lc) throws java.rmi.RemoteException {
		super();
		this.lc = lc;
	}
	
	public String autenticarUsuario(String login, String clave) throws java.rmi.RemoteException {
		if (lc.containsKey(login)) {
		   String[] inf = (String[]) lc.get(login);
		   String claveUsu = inf[0];
		   if (claveUsu.equals(clave)) {
			   return inf[1];
		   } else {
			   return "";
		   }
		} else {
		     return null;
		}
	}
	
	public LinkedList<String> getAliases() {
		LinkedList<String> aliases = new LinkedList<String>();
		Set<String> set = lc.keySet();
		Iterator<String> itr = set.iterator();
		String str = "";
		while (itr.hasNext()) {
			str = itr.next();
			String[] inf = (String[]) lc.get(str);
			aliases.add(inf[1]);
		}
		return aliases;
	}
}