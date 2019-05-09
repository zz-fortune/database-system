package sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sorter {

	/**
	 * 本次实验中共1000000条数据，每条16B，大约16MB，故可采用16路归并
	 * 
	 * 假设一块的大小为60KB，则每块大约有3800条记录。取个整，假设每块有3800条记录
	 */

	private final int maxsize = 62500;
	private final int blocksize = 3800;

	private List<String> tmpFilePaths = new ArrayList<String>();
	private int tmpFileNum = 0;
	private List<BufferedReader> tmpReaders = new ArrayList<BufferedReader>();

	public Sorter() {

	}

	public void sort(String srcFilePath, String dstFilePath) {

		// 	第一阶段，将文件的分块排序
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(srcFilePath)));
			int count = 0;
			String line = null;
			List<String> tmp = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				count++;
				tmp.add(line);
				if (count == this.maxsize) {
					Block block = new Block(tmp);
					tmp = block.selectSort();
					File file = new File(getNewTmpFile());
					file.createNewFile();
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					for (String string : tmp) {
						writer.write(string + "\n");
					}
					writer.close();

					tmp.clear();
					count = 0;
				}
			}
			if (!tmp.isEmpty()) {
				Block block = new Block(tmp);
				tmp = block.selectSort();
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getNewTmpFile())));
				for (String string : tmp) {
					writer.write(string + "\n");
				}
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 	第二阶段，归并排序

		BufferedWriter writer = null;
		try {
			File file = new File(dstFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Block> tobesorted = new ArrayList<Block>();
		createReaders();
		for (int i = 0; i < 16; i++) {
			tobesorted.add(new Block(blocksize));
		}
		int tmpIndex = 0, tmpKey = Integer.MAX_VALUE;
		int remain = 16, resize = 0;
		List<String> re = new ArrayList<String>();
		Block tmpBlock;
		while (remain > 0) {
			tmpKey=Integer.MAX_VALUE;
			for (int i = 0; i < remain; i++) {
				tmpBlock = tobesorted.get(i);
				if (tmpBlock.isEmpty()) {
					if (!updateBlock(tmpBlock, i)) {
						tobesorted.remove(i);
						remain--;
					}
					i--;
				} else {
					if (tmpKey > tmpBlock.firstKey()) {
						tmpKey = tmpBlock.firstKey();
						tmpIndex = i;
					}
				}
			}
			if (remain<1) {
				break;
			}
			resize++;
			re.add(tobesorted.get(tmpIndex).firstRecord());
			if (resize == blocksize) {
				try {
					for (String string : re) {
						writer.write(string + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				resize=0;
				re.clear();
			}
		}
		
		try {
			for (String string : re) {
				writer.write(string + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 	删除临时文件
		deleteTmpFile();
	}

	private boolean updateBlock(Block block, int i) {
		List<String> tmp=new ArrayList<String>();
		BufferedReader reader=tmpReaders.get(i);
		try {
			String line=null;
			int count=0;
			while((line=reader.readLine())!=null) {
				count++;
				tmp.add(line);
				if (count==blocksize) {
					break;
				}
			}
			if (count==0) {
				tmpReaders.remove(i);
				reader.close();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		block.update(tmp);
		return true;
	}

	private String getNewTmpFile() {
		tmpFileNum++;
		String path = "data/tmp" + tmpFileNum + ".txt";
		tmpFilePaths.add(path);
		return path;
	}

	private void deleteTmpFile() {
		for (String string : tmpFilePaths) {
			File file = new File(string);
			file.delete();
		}
		tmpFileNum = 0;
		tmpFilePaths.clear();
	}

	private void createReaders() {
		try {
			for (String string : tmpFilePaths) {
				tmpReaders.add(new BufferedReader(new FileReader(new File(string))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		Sorter sorter = new Sorter();
		long start = System.currentTimeMillis();
		sorter.sort("data/test.txt", "data/re1.txt");
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}
