package can.tools.dbc2xml;

import java.util.ArrayList;

public class Messages {
	/* һ������(message)��ͨ����������ź� */
	ArrayList<Signal> signals;
    String name;
    int length;
    int id; //����ID��
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Signal> getSignals() {
		return signals;
	}

	public void setSignals(ArrayList<Signal> signals) {
		this.signals = signals;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Messages(String name) {
		super();
		this.name = name;
	}

	public Messages(String name, int length) {
		super();
		this.name = name;
		this.length = length;
	}
	
	public Messages(String name, int length, int id) {
		super();
		this.name = name;
		this.length = length;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Messages [signals=" + signals + ", name=" + name + ", length="
				+ length + ", id=" + id + "]";
	}
	
}
