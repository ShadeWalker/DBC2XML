package can.tools.dbc2xml;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signal {
	String name; //�ź�����
	int start_bit;	//��ʼλ
	int length; //����
	String unit;
	double factor;
	double offset;
	String regex_1 = "\\|";
	String regex_2 = "@";
	String regex_3 = "(?=\")[\"\\S]+";
	
	//ʹ��key-value���洢�ź���ֵ��������Ķ�Ӧ��ϵ, �ź���ֵ��Ϊkey����������Ϊvalue
	HashMap<Integer, String> val_table = null;
	
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
				System.out.println("factor: "+factor+" offset: "+offset);
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
		System.out.println("unit: "+unit);
	}
}
