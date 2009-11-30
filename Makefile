Cliente:
	javac -Xlint usu.java Msg.java infoUsuario.java

Servidor:
	javac MsgServer.java

stub: MsgImpl
	rmic MsgImpl

MsgImpl: Msg
	javac MsgImpl.java Msg.java infoUsuario.java

Msg:
	javac Msg.java infoUsuario.java
