import java.util.*;
import java.sql.*;

public class infoUsuario {
    boolean conectado, autenticado;
    LinkedList<String> suscriptores;
    LinkedList<String> mensajes;
		Calendar lastConnection; 

    infoUsuario() {
      conectado = false;
      autenticado = false;
      suscriptores = new LinkedList<String>();
      mensajes = new LinkedList<String>();
			lastConnection = Calendar.getInstance();
    }

    boolean getConectado() {
      return conectado;
    }

    void setConectado(boolean b){
			if (b == true)
				lastConnection = Calendar.getInstance();
      conectado = b;
    }

		String getIntervalo(){
			Calendar cal = Calendar.getInstance();
			long miliseconds = cal.getTimeInMillis() - lastConnection.getTimeInMillis();
			long Hours = miliseconds / (1000*60*60);
			long Minutes = (miliseconds % (1000*60*60)) / (1000*60);
			long Seconds = ((miliseconds % (1000*60*60)) % (1000*60)) / 1000;
			return " " + Hours + " horas, " + Minutes + " minutos and " + Seconds +" segundos.\n";
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
