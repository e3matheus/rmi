import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MsgServer {

  public MsgServer() {
    try {
      //  Usar la linea de abajo para no hacer la llamada a rmiregistry
      //     desde la linea de comandos, de esta forma el mismo programa 
      //     servidor crea su propio servicio de nombres antes de publicar 
      //     el objeto remoto c. 
      LocateRegistry.createRegistry(21001);
      Msg c = new MsgImpl();
      //  Este programa asume que el rmiregistry estara activo en la maquina
      //     akarso, y en el puerto 21000
      Naming.rebind("rmi://akarso.ldc.usb.ve:21001/MsgService", c);
    } catch (Exception e) {
      System.out.println("Trouble: " + e);
    }
  }

  public static void main(String args[]) {
    new MsgServer();
  }
}
