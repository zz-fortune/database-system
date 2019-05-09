package index;

import java.util.List;

/**
 * B+树的节点，包括叶子节点和内部节点
 * 
 * @author zhang heng
 *
 */
public interface Node {
	
	/**
	 * 判断当前节点是否是一个叶子节点
	 * 
	 * @return {@code true} 当且仅当当前节点是叶子节点
	 */
	public boolean isLeaf();
	
	/**
	 * 获得当前节点已经存储的关键字的数目
	 * 
	 * @return 当前节点已经存储的关键字的数目
	 */
	public int getKeyNum();
	
	
	/**
	 * 返回当前节点的第一个 key
	 * 
	 * @return 当前节点的第一个 key
	 */
	public int getFirst() ;
	
	/**
	 * 将当前节点分裂成两个节点
	 * 
	 * @param num 前一个节点的key的数目
	 * @return 分裂生成的新节点
	 */
	public Node splitNode(int num);
	
	/**
	 * 删除本节点中关键字 key。
	 * 
	 * @param key 待删除的关键字
	 * @return {@code true} 如果删除成功
	 */
	public boolean delete(int key) ;
	
	/**
	 * 返回节点的 key 的列表
	 * 
	 * @return
	 */
	public List<Integer> getKeys() ;
}
