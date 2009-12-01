Aute: autenticar
	javac -Xlint aute.java

autenticar: AutenticadorImpl
	rmic AutenticadorImpl

AutenticadorImpl: Autenticador
	javac AutenticadorImpl.java

Autenticador: Cliente
	javac Autenticador.java

Cliente: Servidor
	javac -Xlint usu.java Msg.java infoUsuario.java

Servidor: stub_mensaje
	javac difu.java

stub_mensaje: MsgImpl
	rmic MsgImpl

MsgImpl: Msg
	javac -Xlint MsgImpl.java Msg.java infoUsuario.java

Msg:
	javac Msg.java infoUsuario.java
