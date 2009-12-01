Cliente: Servidor
	javac -Xlint usu.java Msg.java infoUsuario.java

Servidor: stub
	javac difu.java

stub: MsgImpl
	rmic MsgImpl

MsgImpl: Msg
	javac -Xlint MsgImpl.java Msg.java infoUsuario.java

Msg:
	javac Msg.java infoUsuario.java
