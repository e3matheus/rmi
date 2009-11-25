public class Chap {
  private static final int WAITING = 0;
  private static final int SENTCHALLENGE = 1;
  private int state = WAITING;
  infoUsuario us1;
  String challenge;

  Chap (infoUsuario usuario, String login){
    us1 = usuario;
    challenge = login + "23";
  }

  public String processInput(byte[] theInput) throws Exception {
    String theOutput = "";

    if (state == WAITING) {
      theOutput = challenge;
      state = SENTCHALLENGE;
    } else if (state == SENTCHALLENGE ) {
      byte[] retoB = challenge.getBytes();
      byte[] pass = us1.getCF();
      byte[] tot = new byte[pass.length + retoB.length];
      System.arraycopy(pass,0,tot,0,pass.length);
      System.arraycopy(retoB,0,tot,pass.length, retoB.length);
      
      //System.out.println("La clave de cifrado guardada es: " + pass);
     // System.out.println("Input: " + theInput);

      if  (theInput.equals(tot)) {
        //theOutput = Usuario.toString;
        theOutput = "Operacion Exitosa";
        state = WAITING;
      } 

    } else {
      theOutput = "Usuario no esta registrado, Conexion Fallida";
    }

    System.out.println(theOutput);
    return theOutput;
  }        
}
