package sort;

import java.util.ArrayList;
import java.util.List;

public class Block {

	private boolean isEmpty;
	private int size;
	private List<Integer> recordsA = new ArrayList<Integer>();
	private List<String> recordsB = new ArrayList<String>();

	private int current = 0;

	public Block(int size) {
		assert size > 0;
		isEmpty = true;
		this.size = size;
	}

	public Block(List<String> records) {
		isEmpty = false;
		this.size = records.size();
		for (String string : records) {
			if (string.equals("")) {
				continue;
			}
			String[] strings = string.split(",");
			recordsA.add(Integer.valueOf(strings[0]));
			recordsB.add(strings[1]);
		}
	}

	public List<String> getRecords() {
		List<String> re = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			re.add(recordsA.get(i) + "," + recordsB.get(i));
		}
		return re;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	/**
	 * 返回第一条记录的 key
	 * 
	 * @return
	 */
	public int firstKey() {
		return recordsA.get(current);
	}

	/**
	 * 返回第一条记录并删除
	 * 
	 * @return 第一条记录
	 */
	public String firstRecord() {
		assert current < size;
		String string = recordsA.get(current) + "," + recordsB.get(current);
		current++;
		if (current == size) {
			isEmpty = true;
		}
		return string;
	}

	/**
	 * 对本块中的内容进行升序排序，返回排序后的结果。排序算法使用选择排序
	 * 
	 * @return 升序的记录列表
	 */
	public List<String> selectSort() {
		int tmpA;
		String tmpB;
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				if (recordsA.get(i) > recordsA.get(j)) {
					tmpA = recordsA.get(i);
					recordsA.set(i, recordsA.get(j));
					recordsA.set(j, tmpA);
					tmpB = recordsB.get(i);
					recordsB.set(i, recordsB.get(j));
					recordsB.set(j, tmpB);
				}
			}
		}
		return getRecords();
	}

	public void update(List<String> list) {
		recordsA.clear();
		recordsB.clear();
		size = list.size();
		for (String string : list) {
			if (string.equals("")) {
				continue;
			}
			String[] strings = string.split(",");
			recordsA.add(Integer.valueOf(strings[0]));
			recordsB.add(strings[1]);
		}
		isEmpty = false;
		current = 0;
	}
}
