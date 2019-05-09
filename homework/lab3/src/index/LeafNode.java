package index;

import java.util.ArrayList;
import java.util.List;

public class LeafNode implements Node {

	private final boolean isLeaf = true;

	private List<Integer> keys;
	private List<String> values;
	private LeafNode next;
	private LeafNode previous;

	public LeafNode() {
		this.keys = new ArrayList<Integer>();
		this.values=new ArrayList<String>();
	}

	/**
	 * 构造器
	 * 
	 * @param key 节点的第一个 key
	 */
	public LeafNode(int key, String value) {
		this();
		keys.add(key);
		values.add(value);
	}

	/**
	 * 构造器
	 * 
	 * @param keys 节点对应的 key 的列表
	 */
	public LeafNode(List<Integer> keys, List<String> values) {
		this();
		assert keys.size()==values.size();
		for (int i = 0; i < keys.size(); i++) {
			this.keys.add(keys.get(i));
			this.values.add(values.get(i));
		}
	}

	/*****************************
	 * 
	 * getters and setters
	 * 
	 ****************************/

	public LeafNode getNext() {
		return next;
	}

	public LeafNode getPrevious() {
		return previous;
	}

	public void setNext(LeafNode next) {
		this.next = next;
	}

	public void setPrevious(LeafNode previous) {
		this.previous = previous;
	}

	public int getKey(int pos) {
		return keys.get(pos);
	}
	
	public List<String> getValues() {
		return values;
	}

	@Override
	public List<Integer> getKeys() {
		return keys;
	}

	/**
	 * 查询节点中是否存在索引 key
	 * 
	 * @param key 带查询的索引
	 * @return key 对应的值，如果索引存在。否则返回 {@code null}
	 */
	public int search(int key) {
		int pos = this.keys.indexOf(key);
		if (pos == -1) {
			return pos;
		}
		return key;
	}
	
	public boolean insert(int key, String value) {
		int pos=0;
		while(pos<this.keys.size()) {
			if (keys.get(pos)<key) {
				pos++;
			}else if (keys.get(pos)==key) {
				return false;
			}else {
				break;
			}
		}
		this.keys.add(pos, key);
		this.values.add(pos, value);
		return true;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	public int getKeyNum() {
		return this.keys.size();
	}

	public boolean insert(int key) {
		int pos=0;
		while(pos<this.keys.size()) {
			if (keys.get(pos)<key) {
				pos++;
			}else if (keys.get(pos)==key) {
				return false;
			}else {
				break;
			}
		}
		this.keys.add(pos, key);
		return true;
	}

	@Override
	public LeafNode splitNode(int n) {
		LeafNode node = new LeafNode();
		List<Integer> list = new ArrayList<Integer>();
		List<String> list2=new ArrayList<String>();
		for (int i = 0; i < this.keys.size(); i++) {
			if (i < n) {
				node.keys.add(this.keys.get(i));
				node.values.add(this.values.get(i));
			} else {
				list.add(keys.get(i));
				list2.add(values.get(i));
			}
		}
		this.keys = list;
		this.values=list2;
		return node;
	}

	@Override
	public int getFirst() {
		return this.keys.get(0);
	}

	@Override
	public boolean delete(int key) {
		int pos = this.keys.indexOf(key);
		if (pos == -1) {
			return false;
		}
		this.keys.remove(pos);
		return true;
	}

}
