package can.tools.dbc2xml;

import java.util.ArrayList;

public class Nodes {
	String name; //�ڵ�����
	/* һ���ڵ�����ж������� */
	ArrayList<Messages> messages; //�ڵ���Ϣ
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Messages> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<Messages> messages) {
		this.messages = messages;
	}
	
	public Nodes(String name) {
		super();
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Nodes [name=" + name + ", messages=" + messages + "]";
	}
}
