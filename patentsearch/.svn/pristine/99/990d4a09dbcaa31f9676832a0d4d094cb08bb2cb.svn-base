package patentsearch.bean.util.file;


import java.util.LinkedList;

public class Stack {
        private LinkedList list = new LinkedList();
        public void push(StoreDirectory v) {
                list.addFirst(v);
        }
        public void push(Object v) {
                list.addFirst(v);
        }
        public StoreDirectory top() {
                return (StoreDirectory) list.getFirst();
        }
        public Object topObj() {
                return list.getFirst();
        }
        public StoreDirectory pop() {
                return (StoreDirectory) list.removeFirst();
        }
        public Object popObj() {
                return list.removeFirst();
        }
        public String toString() {
                return list.toString();
        }
        public boolean isEmpty() {
                return list.isEmpty();
        }
}