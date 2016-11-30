package patentsearch.bean.util.file;

import java.util.*;
public class SwitchToTreeJson{
        //一个关于父亲节点和所有孩子们的键值对
        private HashMap parentMap = new HashMap();
        //一个关于节点id和节点对象的键值对
        private HashMap nodeMap = new HashMap();
        //包含所有节点的list
        private ArrayList nodeList = new ArrayList();
       
       
        public void addNode(StoreDirectory node){
                nodeList.add(node);
                nodeMap.put(node.getId(), node);
                addParentMap(node.getParent(),node);
        }
       
        //组装成一个节点与父亲id的map
        private void addParentMap(String parentId,StoreDirectory node)
        {
                List list = new ArrayList();
        //如果这个id下面的孩子们
        if (!parentMap.containsKey(parentId)) {
            list.add(node);
            parentMap.put(parentId, list);
        }
        else {
            list = (ArrayList) parentMap.get(parentId);
            list.add(node);
            parentMap.put(parentId, list);
        }
        }
       
        //根据节点id得到节点
        private StoreDirectory getNode(String id)
        {
                return (StoreDirectory)nodeMap.get(id);
        }
       
        //返回全部的父亲节点id组成的一个set
        public Set getParentSet() {
                Set parentSet = new HashSet();
                Iterator it = nodeList.iterator();
                while (it.hasNext()) {
                        StoreDirectory vo = (StoreDirectory) it.next();
                        parentSet.add(vo.getParent());
                }
                return parentSet;
        }
       
        //查找孩子
        public ArrayList getNodesByParentId(String parentId) {
        return (ArrayList) parentMap.get(parentId);
    }
       
        //设置第一层节点。主要把那些找不到父亲的节点主动的放在根节点下面。       
        private void setFistFloor(StoreDirectory root) {
                Set parentSet = this.getParentSet();
                String rootId = root.getId();
                //用来存放不存在的父亲节点的list
                ArrayList notExitslist = new ArrayList();
                //下面查找所有的父节点字符串，如果不存在表明应该准备把这些父亲的孩子转存到虚根的下面，作为虚根的孩子
                Object[] parents = parentSet.toArray();
                for (int temp = 0; temp < parents.length; temp++) {
                        String tempId = parents[temp].toString();
                        //下面找到所有的不存在的父亲节点，放在list里面。
                        if (this.getNode(tempId) == null && !tempId.equals(root.getId())) {
                                notExitslist.add(parents[temp].toString());
                        }
                }
                //下面根据父亲孩子map找到所有的没有找到父亲节点的孩子们，将它们放到虚根的下面。。下面的是当有多个父亲节点不存在的时候进行更改父亲到虚根下面的操作。
                Iterator it = notExitslist.iterator();
                while (it.hasNext()) {
                        ArrayList list2 = getNodesByParentId(it.next().toString());
                        for (int temp = 0; temp < list2.size(); temp++) {
                                StoreDirectory tempNode = (StoreDirectory) list2.get(temp);
                                tempNode.setParent(rootId);
                                addParentMap(rootId, tempNode);
                        }
                }
        }
       
        //返回父亲与孩子们的映射关系
        private Map getParentMap() {
                return parentMap;
        }
       
        public String getTreeJson(SwitchToTreeJson tree,StoreDirectory root){
                StringBuilder ans = new StringBuilder();
                Set parentSet = tree.getParentSet();
                Stack stack = new Stack();
                stack.push(root);
                tree.setFistFloor(root);
                Map map = tree.getParentMap();
                String result = "";
                ans.append("[");
                while(!stack.isEmpty())
                {
                        StoreDirectory e = stack.top();
            //得到该父亲的孩子节点们
            ArrayList childs = (ArrayList) map.get(e.getId());
            Iterator cIt = childs.iterator();
            //设置一个堆栈是否改变的标志位
            boolean stackChanged = false;
            //如果孩子节点循环未结束或者堆栈没有改变，就进行循环孩子节点的操作！
            while (cIt.hasNext() && (!stackChanged)) {
                //得到孩子节点
                StoreDirectory aChild = (StoreDirectory) cIt.next();
                //如果节点没有被打印出来就打印
                if (aChild.getPrint() == 0) {
                    // 如果是树枝节点 ，就直接转换成为json字符串
                    if ((!parentSet.contains(aChild.getId()))) {
                        ans.append(JsonUtil.bean2json(aChild) + ",");
                        //注意节点打印之后有一个逗号！
                        //ans.append(getExtNodeJsonStr(aChild) + ",");
                        //设置节点被打印的标志位。
                        aChild.setPrint(1);
                    }
                    // 如果是非树枝节点就打印一部分字符串，同时入堆栈进行下次的循环
                    else if (parentSet.contains(aChild.getId())) {
                        //使用了一个开源java类用来形成json串。
                        ans.append(JsonUtil.bean2json(aChild));
                        //ans.append(getExtNodeJsonStr(aChild));
                        ans.deleteCharAt(ans.lastIndexOf("}"));
                        ans.append(",\"children\":[");
                        //设置该节点已经打印
                        aChild.setPrint(1);
                        //将该节点推入堆栈
                        stack.push(aChild);
                        //设置堆栈被修改了。将退出当次的while循环。
                        stackChanged = true;
                        break;
                    }
                }
            }
           
            //下面是进行的json数组封闭组串。
            //打印完一个父亲的全部孩子们的json串之后删除最后一个逗号。再加上一个"]".同时扔掉该父亲节点。
            //注意查询条件最后一个是因为没有打印root这个json串，所以没有必要在后面进行数组的封闭操作！
            if ((!cIt.hasNext()) && stackChanged == false) {
                //注意打印完父亲之后要进行字符串的封闭操作。
                if (e.getPrint() == 1) {
                    ans.deleteCharAt(ans.lastIndexOf(","));
                    ans.append("]},");
                }
                //将打印完的父亲节点从堆栈中扔掉。
                stack.pop();
            }
                }
                 //下面进行整个字符串的封闭操作。
        ans.deleteCharAt(ans.lastIndexOf(","));
        ans.append("]");
        result = ans.toString();
        return result;
        }
}