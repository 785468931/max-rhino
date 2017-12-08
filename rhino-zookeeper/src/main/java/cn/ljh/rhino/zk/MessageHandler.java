package cn.ljh.rhino.zk;

public interface MessageHandler {
	String getNodeName();

	void onMsg(String type,String data);
}
