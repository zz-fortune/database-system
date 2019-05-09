package index;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * B+树。定义如 {@linkplain wiki}
 * 
 * @author zhang heng
 *
 */
public class BPlusTree {
	private int CHILDRENS = 10; // 	节点可以拥有的孩子节点总数

	private Node root;

//	private Node current;
	private Stack<Node> parentStack;
	private Stack<Integer> childrenStack;
	private int layer;

	/**
	 * 构造器
	 */
	public BPlusTree() {
		root = null;
		layer = 0;
		parentStack = new Stack<Node>();
		childrenStack = new Stack<Integer>();
	}

	/**
	 * 插入一条索引，要求待插入的 key 不能重复
	 * 
	 * @param key 待插入的 key
	 * @return {@code true} 如果插入成功
	 */
	public boolean insert(int key, String value) {

		// 	如果是空树，建立一个叶子节点作为根节点，并插入
		if (root == null) {
			root = new LeafNode(key, value);
			layer++;
			return true;
		}

		// 	找到要插入的位置，以及从根节点到该位置的路径
		find(key);

		
		Node current = this.parentStack.pop();
		// 	先将节点插入，若是key重复，直接返回失败
		if (!((LeafNode)current).insert(key,value)) {
			return false;
		}
		
		//	 若是对应的叶子节点没满，则直接插入
			
		if (current.getKeyNum() <= this.CHILDRENS) {
			return true;
		}

		// 	拆分当前节点（叶节点），并串联起来
		Integer upkey;
		LeafNode node = ((LeafNode) current).splitNode(CHILDRENS / 2);
		upkey = current.getFirst();
		if (((LeafNode) current).getPrevious()!=null) {
			((LeafNode) current).getPrevious().setNext(node);
		}
		node.setPrevious(((LeafNode) current).getPrevious());
		node.setNext((LeafNode) current);
		((LeafNode) current).setPrevious(node);

		// 	处理非叶节点的插入，可能需要多次向上层插入
		Node node1 = node;
		Node node2 = current;
		while (upkey != null) {
			IntraNode parent = (IntraNode) parentStack.pop();
			int child = childrenStack.pop();
			if (parent == null) { // 如果没有父节点，则新建一个内部节点作为父节点，同时，这个节点也就是根节点
				root = new IntraNode(upkey);
				((IntraNode) root).setChild(0, node1);
				((IntraNode) root).setChild(1, node2);
				layer++;
				upkey = null;
			} else if (parent.getKeyNum() < CHILDRENS) { // 如果父节点没满，则直接插入即可
				parent.insert(upkey);
				parent.setChild(child, node1);
				parent.setChild(child + 1, node2);
				upkey = null;
			} else { // 如果父节点已满，则分裂父节点。这里也需要上移一个 key，上移后当前节点不保留这个上移的 key
				parent.insert(upkey);
				parent.setChild(child, node1);
				parent.setChild(child + 1, node2);
				node1 = parent.splitNode(CHILDRENS / 2);
				upkey = parent.getFirst();
				node2 = parent;
			}
		}
		parentStack.clear();
		childrenStack.clear();
		return true;
	}

	/**
	 * 记录查询一个key对应的路径
	 * 
	 * @param key 待查询的key
	 */
	private void find(int key) {
		parentStack.push(null);
		childrenStack.push(0);

		Node current = root;
		while (!current.isLeaf()) {
			parentStack.push(current);
			int c = ((IntraNode) current).search(key);
			childrenStack.push(c);
			current = ((IntraNode) current).getChild(c);
		}
		parentStack.push(current);
	}

	/**
	 * 删除一个key对应的索引
	 * 
	 * @param key 待删除的key
	 * @return {@code true}，如果删除成功
	 */
	public boolean delete(int key) {
		if (layer == 0) {
			return false;
		}
		find(key);
		LeafNode current = (LeafNode) parentStack.pop();

		// 	如果关键字存在，直接返回失败
		if (!current.delete(key)) {
			return false;
		}

		if (layer == 1 && current.getKeyNum() == 1) {
			root = null;
			layer = 0;
		}

		// 	如果删除后，节点的 key 依旧满足要求，直接返回成功
		if (current.getKeyNum() >= (CHILDRENS / 2) || layer == 1) {
			return true;
		}

		// 	向兄弟节点借一个 key
		IntraNode parent = (IntraNode) parentStack.pop();
		int child = childrenStack.pop();
		LeafNode lbro = (LeafNode) parent.getChild(child - 1);
		LeafNode rbro = (LeafNode) parent.getChild(child + 1);

		// 	如果左兄弟有多余的 key
		if (lbro != null && lbro.getKeyNum() > (CHILDRENS / 2)) {
			int k = lbro.getKey(lbro.getKeyNum() - 1);
			current.insert(k);
			lbro.delete(k);
			parent.setKey(k, child - 1);
			return true;
		}

		// 	如果有兄弟有多余的 key
		if (rbro != null && rbro.getKeyNum() > (CHILDRENS / 2)) {
			int k = rbro.getKey(0);
			current.insert(k);
			rbro.delete(k);
			parent.setKey(k, child);
			return true;
		}

		// 	合并两个叶子节点
		LeafNode node;
		Integer downkey;
		if (lbro != null) {
			node = mergeLeafNode(lbro, current);

			// 	将叶子节点串联起来
			if (lbro.getPrevious() != null) {
				lbro.getPrevious().setNext(node);
			}
			node.setPrevious(lbro.getPrevious());
			node.setNext(current.getNext());
			if (current.getNext() != null) {
				current.getNext().setPrevious(node);
			}
			downkey = child - 1;
		} else {
			node = mergeLeafNode(current, rbro);

			// 	将叶子节点串联起来
			if (current.getPrevious() != null) {
				current.getPrevious().setNext(node);
			}
			node.setPrevious(current.getPrevious());
			node.setNext(rbro.getNext());
			if (rbro.getNext() != null) {
				rbro.getNext().setPrevious(node);
			}
			downkey = child;
		}

		IntraNode cur;
		Node n = node;
		while (downkey != null) {
			cur = parent;
			parent = (IntraNode) parentStack.pop();
			child = childrenStack.pop();

			// 	如果当前节点是根节点，且根节点只有一个 key，删除根节点
			if (parent == null && cur.getKeyNum() == 1) {
				root = n;
				layer--;
				break;
			}

			// 	当前结点是根节点，且根节点不只有一个 key；或者当前节点的的 key 足够
			if (parent == null || cur.getKeyNum() > (CHILDRENS / 2)) {
				cur.delete(downkey);
				cur.setChild(downkey, n);
				break;
			}

			// 	检查兄弟节点
			IntraNode l = (IntraNode) parent.getChild(child - 1);
			IntraNode r = (IntraNode) parent.getChild(child + 1);

			// 	如果左兄弟节点有多余的 key
			if (l != null && l.getKeyNum() > (CHILDRENS / 2)) {
				Node childNode = l.getChild(l.getKeyNum());
				int k = parent.getKey(child - 1);
				parent.setKey(l.getKey(l.getKeyNum() - 1), child - 1);
				cur.delete(downkey);
				cur.insert(k);
				cur.setChild(0, childNode);
				l.delete(l.getKey(l.getKeyNum() - 1));
				cur.setChild(downkey + 1, n);
				break;
			}

			// 	如果右兄弟节点有多余的 key
			if (r != null && r.getKeyNum() > (CHILDRENS / 2)) {
				Node childNode = r.getChild(0);
				int k = parent.getKey(child);
				parent.setKey(r.getKey(0), child);
				cur.delete(downkey);
				cur.insert(k);
				cur.setChild(cur.getKeyNum(), childNode);
				r.delete(0);
				cur.setChild(downkey, n);
				break;
			}

			// 	合并兄弟节点
			if (l != null) {
				int k = parent.getKey(child - 1);
				cur.delete(downkey);
				cur.insert(k);
				cur.setChild(downkey + 1, n);
				n = mergeIntroNode(l, cur);
				downkey = child - 1;
			} else {
				int k = parent.getKey(child);
				cur.delete(downkey);
				cur.insert(k);
				cur.setChild(downkey, n);
				cur.setChild(cur.getKeyNum(), r.getChild(0));
				n = mergeIntroNode(cur, r);
				downkey = child;
			}

		}
		parentStack.clear();
		childrenStack.clear();
		return true;
	}

	/**
	 * 查询
	 * 
	 * @param key 待查询的 key
	 * @return key 对应的记录，如果索引中存在该key。否则返回{@code null}
	 */
	public int search(int key) {
		find(key);
		LeafNode leaf = (LeafNode) parentStack.pop();
		parentStack.clear();
		childrenStack.clear();
		return leaf.search(key);
	}

	/**
	 * 合并两个叶子节点
	 * 
	 * @param node1 叶子节点1
	 * @param node2 叶子节点2
	 * @return 合并后的叶子节点
	 */
	private LeafNode mergeLeafNode(LeafNode node1, LeafNode node2) {
		List<Integer> list11 = node1.getKeys();
		List<String> list12=node1.getValues();
		List<Integer> list21 = node2.getKeys();
		List<String> list22=node2.getValues();
		list11.addAll(list21);
		list12.addAll(list22);
		LeafNode node = new LeafNode(list11, list12);
		return node;
	}

	/**
	 * 合并连个内部节点。其中第二个内部节点的第一个孩子节点会被直接丢弃
	 * 
	 * @param node1 内部节点1
	 * @param node2 内部节点2
	 * @return 合并后的内部节点
	 */
	private IntraNode mergeIntroNode(IntraNode node1, IntraNode node2) {
		List<Integer> keys = node1.getKeys();
		keys.addAll(node2.getKeys());
		List<Node> nodes1 = node1.getChildrens();
		List<Node> nodes2 = node2.getChildrens();
		nodes2.remove(0);
		nodes1.addAll(nodes2);
		IntraNode node = new IntraNode(keys, nodes1);
		return node;
	}

	public void print() {
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int len = queue.size();
			Node current;
			for (int i = 0; i < len; i++) {
				current = queue.poll();
				System.out.print(current.getKeys());
				System.out.print("\t");
				if (!current.isLeaf()) {
					List<Node> nodes = ((IntraNode) current).getChildrens();
					for (Node node : nodes) {
						queue.offer(node);
					}
				}
			}
			System.out.println("\n");
		}
	}

	public static void main(String[] args) {
		BPlusTree tree = new BPlusTree();
		for (int i = 0; i < 11; i++) {
			tree.insert(i,"1");
		}
		tree.delete(5);
		tree.delete(8);
		tree.print();
	}
}
