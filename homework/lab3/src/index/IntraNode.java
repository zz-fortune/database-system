package index;

import java.util.ArrayList;
import java.util.List;

public class IntraNode implements Node {

	private final boolean isLeaf = false;

	private List<Integer> keys;
	private List<Node> childrens;

	/**
	 * 构造器
	 */
	public IntraNode() {
		this.keys = new ArrayList<Integer>();
		this.childrens = new ArrayList<Node>();
	}

	/**
	 * 构造器
	 * 
	 * @param key
	 */
	public IntraNode(int key) {
		this();
		keys.add(key);
		childrens.add(null);
		childrens.add(null);
	}

	/**
	 * 构造器
	 * 
	 * @param keys  节点的 key 列表（按序）
	 * @param nodes 节点的孩子节点列表（按序）
	 */
	public IntraNode(List<Integer> keys, List<Node> nodes) {
		this();
		this.keys.addAll(keys);
		this.childrens.addAll(nodes);
	}

	/**
	 * 搜索 key，得到 key 的索引在当前节点的哪个孩子节点上
	 * 
	 * @param key 待搜索的 key
	 * @return 可能包含 key 的孩子节点的编号
	 */
	public int search(int key) {
		int pos = 0;
		while (pos < this.keys.size()) {
			if (key >= keys.get(pos)) {
				pos++;
			} else {
				break;
			}
		}
		return pos;
	}

	/*******************************************
	 * 
	 * getters and setters
	 * 
	 ******************************************/

	public boolean setChild(int pos, Node child) {
		assert pos >= 0;

		if (pos >= this.childrens.size() || pos < 0) {
			return false;
		} else {
			this.childrens.set(pos, child);
			return true;
		}

	}

	public void setKey(int key, int pos) {
		assert pos >= 0 && pos < this.keys.size();
		this.keys.set(pos, key);
	}

	public Node getChild(int pos) {
		assert pos >= 0;
		if (childrens.size() > pos && pos >= 0) {
			return childrens.get(pos);
		}
		return null;
	}

	public int getKey(int pos) {
		return keys.get(pos);
	}

	@Override
	public List<Integer> getKeys() {
		return keys;
	}

	public List<Node> getChildrens() {
		return childrens;
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
			if (key<keys.get(pos)) {
				pos++;
			}else {
				break;
			}
		}
		this.keys.add(pos, key);
		if (pos==this.keys.size()) {
			this.childrens.add(null);
		}else {
			this.childrens.add(pos, null);
		}
		return true;
	}

	@Override
	public int getFirst() {
		return keys.get(0);
	}

	@Override
	public IntraNode splitNode(int num) {
		IntraNode node = new IntraNode();
		List<Integer> list1 = new ArrayList<Integer>();
		List<Node> list2 = new ArrayList<Node>();
		for (int i = 0; i < this.keys.size(); i++) {
			if (i < num) {
				node.keys.add(this.keys.get(i));
				node.childrens.add(this.childrens.get(i));
			} else {
				list1.add(keys.get(i));
				list2.add(childrens.get(i));
			}
		}
		node.childrens.add(list2.get(0));
		list2.add(childrens.get(childrens.size() - 1));
		this.keys = list1;
		this.childrens = list2;
		return node;
	}

	/**
	 * 删除对应位置的 key。删除后该 key 对应 的两个孩子节点可能会被直接删除
	 * 
	 * @param pos 待删除的 key 的位置
	 */
	@Override
	public boolean delete(int pos) {
		assert pos >= 0 && pos < this.keys.size();
		this.keys.remove(pos);
		this.childrens.remove(pos);
		return true;
	}

}
