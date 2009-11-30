import java.util.*;

public class infoUsuario {
    @SuppressWarnings("unchecked") 
    String clave, alias;
    LinkedList<String> suscriptores = new LinkedList<String>();

    infoUsuario(String clave, String alias) throws Exception {
      this.clave = clave;
      this.alias = alias;
    }

    String getAlias() {
      return alias;
    }

    LinkedList getSuscriptores() {
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
