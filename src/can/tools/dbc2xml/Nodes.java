package can.tools.dbc2xml;

public class Nodes {
	String name; //�ڵ�����
	int id; //�ڵ�ID��
	Messages messages; //�ڵ���Ϣ
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Messages getMessages() {
		return messages;
	}
	public void setMessages(Messages messages) {
		this.messages = messages;
	}
	public Nodes(String name) {
		super();
		this.name = name;
	}
	
}
