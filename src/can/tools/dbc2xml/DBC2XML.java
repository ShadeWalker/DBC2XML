package can.tools.dbc2xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class DBC2XML {
	
	public enum DataType {
		NETWORK_NODE("BU_:"),
		MESSAGE("BO_"),
		SIGNAL("SG_"),
		ATTRIBUTE("BA_"),
		COMMENT("CM_"),
		VALUE_TABLE("VAL_");
	    
	    private String text;
	    
	    DataType(String text) {
	        this.text = text;
	    }
	    
	    public String getText() {
	        return this.text;
	    }
	    
	    // Implementing a fromString method on an enum type
	    private static final Map<String, DataType> stringToEnum = new HashMap<String, DataType>();
	    
	    static {
	        // Initialize map from constant name to enum constant
	        for(DataType blah : values()) {
	            stringToEnum.put(blah.toString(), blah);  //Key-Value: String-Blsh
	        }
	    }
	    
	    // Returns Blah for string, or null if string is invalid
	    public static DataType fromString(String symbol) {
	        return stringToEnum.get(symbol);
	    }

	    @Override
	    public String toString() {
	        return text;
	    }
	}
	
	String path_source;
	ArrayList<String> dataType;
	ArrayList<Nodes> network_nodes; 
	
	public DBC2XML(String path_source) {
		super();
		this.path_source = path_source;
		this.dataType = new ArrayList<>();
		
	}

	public ArrayList<Nodes> read(){
		FileReader fr = null;
		ArrayList<Nodes> nodes = new ArrayList<>();
		
		try{
			fr = new FileReader(this.path_source);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("file don't exist, pleae checck the path is correct or not?");
		}
		
		Scanner sr = new Scanner(fr);
		String str = sr.nextLine();
		int message_index = 0;
		while(null != str){
			String[] sb = str.split("\\s+");
			DataType dataType = DataType.fromString(sb[0]);
			
			if(null != dataType) {
				if(' ' != str.charAt(0)) { //�Էǿո�ͷ����
					switch(dataType) {
						case NETWORK_NODE:
							for(int i=1; i<sb.length; i++){
								nodes.add(new Nodes(sb[i])); //���ýڵ�����
							}
							break;
							
						case MESSAGE:
							for(message_index=0; message_index<nodes.size(); message_index++){
								if(nodes.get(message_index).getName() == sb[(sb.length-1)]){
									nodes.get(message_index).setId(Integer.parseInt(sb[1])); //���ýڵ�ID
									nodes.get(message_index).setMessages(new Messages(sb[2].substring(0, sb[2].length()-2), Integer.parseInt(sb[3]))); //����message������ �ͳ���(��Ҫȥ�������":"��)
								}
							}
							break;
							
						default :
							break;
					}
				} else { //�Կո�ͷ����
					switch(dataType) {
					case SIGNAL:
						Signal signal = new Signal(sb[2]); //signal_name
						signal.setStart_bitAndLengthfromString(sb[4]); //start_bit, length
						if(nodes.get(message_index).getMessages().getSignals() == null){
							ArrayList<Signal> al = new ArrayList<>();
							nodes.get(message_index).getMessages().setSignals(al);
						} 
						nodes.get(message_index).getMessages().getSignals().add(signal);
						break;
						
					case ATTRIBUTE:
						break;
					
					case VALUE_TABLE:
						break;
						
					default :
						break;
					}
				} 
			}
			
			str = sr.nextLine();
		}
		return nodes;
	}
	
	public void generateXML(ArrayList<Nodes> nodes_list){
		
		try {
			SAXTransformerFactory factory =  (SAXTransformerFactory)SAXTransformerFactory.newInstance();
			TransformerHandler handler = null;
			handler = factory.newTransformerHandler();
			Transformer info = handler.getTransformer();
			info.setOutputProperty(OutputKeys.INDENT, "yes");// �Ƿ��Զ���Ӷ���Ŀհ�
			info.setOutputProperty(OutputKeys.ENCODING, "utf-8");// �����ַ�����
			info.setOutputProperty(OutputKeys.VERSION, "1.0");
			// ���洴����can_db.xml
			StreamResult result = new StreamResult(new FileOutputStream(new File("src/can/tools/dbc2xml/can_db.xml")));
			handler.setResult(result);
			
			handler.startDocument();// ��ʼxml
			AttributesImpl impl = new AttributesImpl();
			impl.clear();
			handler.startElement("", "", "node", impl);  //Ԫ�ر�ǩ��ʼ node
			for(int i=0; i<nodes_list.size(); i++){	//�����ڵ�
				Nodes node = nodes_list.get(i);
				//����<node name="xx">
				impl.clear(); //�������
	            impl.addAttribute("", "", "name", "", node.getName());//ΪnodeԪ�����name����
	            handler.startElement("", "", "message", impl); 
	            
	            //����<message name="xx">xx</message>Ԫ��
	            impl.addAttribute("", "", "name", "", node.getMessages().getName());//ΪmessageԪ��name����          
	            ArrayList<Signal> signals = node.getMessages().getSignals();
	            
	            //����<signal>xx</signal>Ԫ��
	            for(int j=0; j<signals.size(); j++){
	            	impl.clear(); 
	            	handler.startElement("", "", "signal", impl);
	            	
	            	handler.endElement("", "", "name"); 
	            	
	            	handler.startElement("", "", "name", impl); 
	            	
	            	handler.startElement("", "", "start_bit", impl); 
	            	handler.endElement("", "", "start_bit"); 
	            	
	            	handler.startElement("", "", "length", impl); 
	            	handler.endElement("", "", "length"); 

	 	            handler.endElement("", "", "signal"); 
	            }
	            impl.clear(); 
	            //����</message>
	            handler.endElement("", "", "message");
	            
			}
			//����</bookstore>
			handler.endElement("", "", "node");
			handler.endDocument(); // ����xml
			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
