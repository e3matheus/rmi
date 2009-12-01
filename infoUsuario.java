import java.util.*;

public class infoUsuario {
    boolean conectado, autenticado;
    LinkedList<String> suscriptores;
    LinkedList<String> mensajes;

    infoUsuario() {
      conectado = false;
      autenticado = false;
      suscriptores = new LinkedList<String>();
      mensajes = new LinkedList<String>();
    }

    boolean getConectado() {
      return conectado;
    }

    void setConectado(boolean b){
      conectado = b;
    }

    boolean getAutenticado() {
      return autenticado;
    }

    void setAutenticado(boolean b){
      autenticado = b;
    }

    LinkedList<String> getSuscriptores() {
      return suscriptores;
    }

    LinkedList<String> getMensajes() {
      return mensajes;
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

    //Agregar Mensajes	
    void agregarM(String msj) {
      mensajes.add(msj);
      return;
    }

    //Obtener primer mensaje
    String obtenerM(){
      if (mensajes.size() > 0)
        return mensajes.removeFirst();
      else return null;
    }
}
