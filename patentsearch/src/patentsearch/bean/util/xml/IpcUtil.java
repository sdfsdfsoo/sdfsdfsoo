package patentsearch.bean.util.xml;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import patentsearch.bean.base.ClassNavEntity;
import patentsearch.utils.base.StringUtil;
import patentsearch.web.action.base.IpcAction;

public class IpcUtil {

	/**
	 * @param args
	 */
	public static List<ClassNavEntity> searchXml(String type, String key){
		List<ClassNavEntity> list = new ArrayList<ClassNavEntity>();
		SAXReader reader = new SAXReader();
		//IPC、ADM类型标识
		Integer typeFlag=0;
		String path = null;
		if ("IPC".equals(type.toUpperCase())){
			path=IpcAction.class.getClassLoader().getResource("IPCTree.xml").getPath();
			typeFlag=0;
		}
		else if ("ADM".equals(type.toUpperCase())) {
			path=IpcAction.class.getClassLoader().getResource("ADMTree.xml").getPath();
			typeFlag=1;
		}
			
		try {
			Document doc = reader.read(new File(path.substring(1)));
			Element root = doc.getRootElement();
			List<Element> nodelist = root.selectNodes("//ipc");
			for(Element e:nodelist){
				 
				if(Pattern.matches("^.*"+key.toUpperCase()+".*$", e.attributeValue("name"))){
				ClassNavEntity o = new ClassNavEntity();
				String ipc = e.attributeValue("IPC");
				o.setClassType(ipc);
				o.setType(typeFlag);
				String des = e.attributeValue("name");
				o.setDes(des.substring(ipc.length()));
				if (e.hasContent()) {
					o.setHasChild(true);
				} else
					o.setHasChild(false);
				list.add(o);}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public static List<ClassNavEntity> parseXml(String type, String nodeStr) {
		List<ClassNavEntity> objects = new ArrayList<ClassNavEntity>();
		SAXReader reader = new SAXReader();
		//IPC、ADM类型标识
		Integer typeFlag=0;
		try {
			String path = null;
			if ("IPC".equals(type.toUpperCase())) {
				path=IpcAction.class.getClassLoader().getResource("IPCTree.xml").getPath();
				typeFlag=0;
			} else if ("ADM".equals(type.toUpperCase())) {
				path=IpcAction.class.getClassLoader().getResource("ADMTree.xml").getPath();
				typeFlag=1;
			}
			String searchNode = "//ipc[@IPC='" + nodeStr.toUpperCase() + "']";
			 
			Document document = reader.read(new File(path.substring(1)));
			Node node = document.selectSingleNode(searchNode);
			if(node==null){
				return new ArrayList<ClassNavEntity>();
			}
			List<Node> nodes = new ArrayList<Node>();
			List<Node> nodesFinal = new ArrayList<Node>();
			Node _node = node;
			while (_node.getParent() != null) {
				nodes.add(_node);
				Element ele = (Element) _node;
				_node = _node.getParent();
			}
			;
			for (int i = nodes.size() - 1; i >= 0; i--) {
				nodesFinal.add(nodes.get(i));
			}
			List<Node> child = ((Element) node).elements("ipc");
			for (Node node1 : child) {
				nodesFinal.add(node1);
			}
			for (Node n : nodesFinal) {
				ClassNavEntity o = new ClassNavEntity();
				String ipc = ((Element) n).attributeValue("IPC");
				o.setClassType(ipc);
				o.setType(typeFlag);
				String des = ((Element) n).attributeValue("name");
				o.setDes(des.substring(ipc.length()));
				if (n.hasContent()) {
					o.setHasChild(true);
				} else
					o.setHasChild(false);
				objects.add(o);
			}
			for(int i=0;i<nodes.size();i++){
				if("A".equals(nodeStr)||"B".equals(nodeStr)||"C".equals(nodeStr)||"D".equals(nodeStr)||"E".equals(nodeStr)||"F".equals(nodeStr)||"G".equals(nodeStr)||"H".equals(nodeStr)){  
					objects.get(i).setMenuFlag(true);
				}else{
					objects.get(i).setMenuFlag(false);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		
		}
//		if("A".equals(nodeStr)||"B".equals(nodeStr)||"C".equals(nodeStr)||"D".equals(nodeStr)||"E".equals(nodeStr)||"F".equals(nodeStr)||"G".equals(nodeStr)||"H".equals(nodeStr)){    //判断nodeStr是数字还是字母
//			
//		}
		return objects;
	}

	public static void main(String[] args) {

		SAXReader reader = new SAXReader();
		try {
			URL path = Thread.currentThread().getContextClassLoader()
					.getResource("ADMTree.xml");
			Document document = reader.read(path.getFile());
			 
			Node node = document.selectSingleNode("//ipc[@IPC='01-01-C0098']");
			List<Node> nodes = new ArrayList<Node>();
			List<Node> nodesFinal = new ArrayList<Node>();
			Node _node = node;
			while (_node.getParent() != null) {
				nodes.add(_node);
				Element ele = (Element) _node;
				_node = _node.getParent();
			}
			for (int i = nodes.size() - 1; i >= 0; i--) {
				nodesFinal.add(nodes.get(i));
			}
			List<Node> child = ((Element) node).elements("ipc");
			for (Node node1 : child) {
				nodesFinal.add(node1);
			}
			List<ClassNavEntity> objects = new ArrayList<ClassNavEntity>();
			for (Node n : nodesFinal) {
				ClassNavEntity o = new ClassNavEntity();
				String ipc = ((Element) n).attributeValue("IPC");
				o.setClassType(ipc);
				String des = ((Element) n).attributeValue("name");
				o.setDes(des.substring(ipc.length()));
				if (n.hasContent()) {
					o.setHasChild(true);
				} else
					o.setHasChild(false);
				objects.add(o);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		IpcUtil.parseXml("IPC", "A");
	}

}
