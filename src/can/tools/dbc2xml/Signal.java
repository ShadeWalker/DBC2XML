package can.tools.dbc2xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signal {
	String name;    //�ź�����
	int start_bit;	//��ʼλ
	int length;     //����
	String unit;    //��λ
	double factor;  //�˻�����
	double offset;  //ƫ����
	
	//ʹ��key-value���洢�ź���ֵ��������Ķ�Ӧ��ϵ
	HashMap<Integer, String> val_table = null; //�ź���ֵ��Ϊkey����������Ϊvalue
	ArrayList<String> al = null;
	String regex_1 = "\\|";
	String regex_2 = "@";
	String regex_3 = "(?=\")[\"\\S]+";   //ƥ��˫�����е�����
	String regex_4 = "((?<=\\s\").*?(?=\"))|(\\w+)"; //�����������еĿո����ƥ��
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStart_bit() {
		return start_bit;
	}
	public void setStart_bit(char start_bit) {
		this.start_bit = start_bit;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public HashMap<Integer, String> getVal_table() {
		return val_table;
	}
	public void setVal_table(HashMap<Integer, String> val_table) {
		this.val_table = val_table;
	}
	public double getFactor() {
		return factor;
	}
	public void setFactor(double factor) {
		this.factor = factor;
	}
	public double getOffset() {
		return offset;
	}
	public void setOffset(double offset) {
		this.offset = offset;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Signal(String name, char start_bit, int length,
			HashMap<Integer, String> val_table) {
		super();
		this.name = name;
		this.start_bit = start_bit;
		this.length = length;
		this.val_table = val_table;
	}
	
	public Signal(String name) {
		super();
		this.name = name;
	}
	
	public void setStart_bitAndLengthfromString(String str){
		String[] str_sig_format_1 = str.split(regex_1);
		this.start_bit = Integer.parseInt(str_sig_format_1[0]);
		String[] str_sig_format_2 = str_sig_format_1[str_sig_format_1.length-1].split(regex_2);
		this.length = Integer.parseInt(str_sig_format_2[0]);
	}
	
	public void setFactorandOffset(String str) {
		/* ������ʽ  "(?<=\\()[^\\)]+"
		 * (?<=\\()  ��ʾ����ƥ��   '('���������, [^\\)]+ ��ʾƥ�� ��')'֮����κ��ַ�   */
		Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");  
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			String s[] = matcher.group().split(",");
			if(s.length >= 2){
				this.factor = Float.parseFloat(s[0]);
				this.offset = Float.parseFloat(s[1]);
				//System.out.println("factor: "+factor+" offset: "+offset);
			}
		}
	}
	
	public void setUnitfromString(String str) {
		Pattern pattern = Pattern.compile(regex_3);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			String s[] = matcher.group().split("\"");
			if(s.length >= 2)
			    this.unit = s[1];
			else
				this.unit = " ";
		}
		//System.out.println("unit: "+unit);
	}
	
	public void setVauleTable(String str, ArrayList<Nodes> nodes) {
		al = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex_4);
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()){
			//System.out.println(matcher.group());
			al.add(matcher.group());
		}
		//System.out.println("message id: " + al.get(1) + " signal name: "+al.get(2));
		/* �ҳ���ID��������message */
		for(int i=0; i<nodes.size(); i++)
		{
			if(null == nodes.get(i).getMessages())
				continue;
			for(int j=0; j<nodes.get(i).getMessages().size(); j++)
			{
				//System.out.println(nodes.get(i).getMessages().get(j).getId());
				if(nodes.get(i).getMessages().get(j).getId() == Integer.parseInt(al.get(1)))  //����ID�ҵ���Ӧ�ı���
				{
					//System.out.println("message id: " + al.get(1));
					//System.out.println("signal num: " + nodes.get(i).getMessages().get(j).getSignals().size());
					for(int k=0; k<nodes.get(i).getMessages().get(j).getSignals().size(); k++)
					{
						//System.out.println("signal name: " + nodes.get(i).getMessages().get(j).getSignals().get(k).getName());
						if(nodes.get(i).getMessages().get(j).getSignals().get(k).getName().equals(al.get(2))) //�ҵ���Ӧ���ź�
						{
							//System.out.println("signal match: " + al.get(2));
							for(int m=0; m<(al.size()-3)/2; m++)
							{
								if(null == nodes.get(i).getMessages().get(j).getSignals().get(k).getVal_table())
								{
									nodes.get(i).getMessages().get(j).getSignals().get(k).setVal_table(new HashMap<Integer, String>());
								}
								nodes.get(i).getMessages().get(j).getSignals().get(k).getVal_table().put(Integer.parseInt(al.get(2*m+3)), al.get(2*m+4));
								//System.out.println("signal name: "+al.get(2)+" key: "+ al.get(2*m+3) +" value: "+al.get(2*m+4));
							}
							//System.out.println("end");
							break;
						}
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "Signal [name=" + name + ", start_bit=" + start_bit
				+ ", length=" + length + ", unit=" + unit + ", factor="
				+ factor + ", offset=" + offset + "]";
	}
	
	
}
