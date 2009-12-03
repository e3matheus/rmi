import java.util.*;
import java.security.*;

public class AutenticadorImpl extends java.rmi.server.UnicastRemoteObject
	implements Autenticador {
	
	Hashtable<String, String[]> lc = new Hashtable<String, String[]>();
	static final int WAITING = 0;
    static final int SENTCHALLENGE = 1;
    int state = WAITING;
	
	public AutenticadorImpl(Hashtable<String, String[]> lc) throws java.rmi.RemoteException {
		super();
		this.lc = lc;
	}
	
	public String autenticarUsuario(String login, String clave) throws java.rmi.RemoteException {
		String challenge = login + "28";
		
		if (state == WAITING) {
			state = SENTCHALLENGE;
			return challenge;
		} else 
			if (state == SENTCHALLENGE ) {
				state = WAITING;
				if (lc.containsKey(login)) {
					String[] inf = (String[]) lc.get(login);
					String claveUsu = inf[0], clavecf = "";
					try {
						clavecf = claveUsu + challenge;
						MessageDigest t_md5 = MessageDigest.getInstance("MD5");
						t_md5.update(clavecf.getBytes());
						byte[] ccf = t_md5.digest();
						StringBuffer sb = new StringBuffer();
						for( int i = 0 ; i < ccf.length ; i++ ) {
							String t = "0"+Integer.toHexString( (0xff & ccf[i]));
							sb.append(t.substring(t.length()-2));
						}
						String sclave = sb.toString();
						if  (sclave.equals(clave)) {
							System.out.println("login: '"+login+"' clave: '"+clave+"' alias: '"+inf[1]+"' Autenticacion Exitosa");
							return inf[1];
						} else {
							System.out.println("login: '"+login+"' clave: '"+clave+"' alias: '"+inf[1]+"' Autenticacion Fallo: Clave Incorrecta");
							return "";
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				} else {
					System.out.println("login: '"+login+"' clave: '"+clave+"' Autenticacion Fallo: No se encuentra el login especificado");
					return null;
				}
			}
		return null;
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
