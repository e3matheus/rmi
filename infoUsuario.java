import java.util.*;

public class infoUsuario {
    String clave, alias;
    LinkedList<String> suscriptores = new LinkedList<String>();
    private byte[] cf;
    byte[] b;

    infoUsuario(String clave, String alias) throws Exception {
      this.clave = clave;
      this.alias = alias;
      this.b = this.clave.getBytes();
      java.security.MessageDigest t_md5 = java.security.MessageDigest.getInstance("MD5");
      this.cf = t_md5.digest(b);
    }

    String getClave() {
      return clave;
    }

    byte[] getClaveBytes() {
      return b;
    }

    String getAlias() {
      return alias;
    }

    byte[] getCF(){
      return cf;  
    }

    LinkedList<String> getSuscriptores() {
      return suscriptores;
    }

    boolean agregarS(String susc) {
      if (!(suscriptores.contains(susc))){
        suscriptores.add(susc);
        return true;
      } else {return false;}
    }

    boolean eliminarS(String susc){
      if ((suscriptores.contains(susc))){
        suscriptores.remove(susc);
        return true;
      } else {return false;}
    }
}
