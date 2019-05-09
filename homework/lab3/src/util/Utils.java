package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {

	/**
	 * 数据生成，每条数据包含两个属性 A、B，A是整数（4字节），B是字符串（12字节）。此处模拟，每条数据存一行
	 * 
	 * @param filename 存储文件
	 * @param num 生成的记录数
	 */
	public static void generateData(String filename, int num) {
		List<String> list = new ArrayList<String>();
		Set<Integer> gd = new HashSet<Integer>();
		Random random = new Random();
		for (int i = 0; i < num; i++) {
			Integer rand = random.nextInt(Integer.MAX_VALUE);
			if (gd.add(rand)) {
				list.add(rand.toString() + ",this is test\n");
			}else {
				i--;
			}
		}
		BufferedWriter writer = null;
		File file = new File(filename);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			writer=new BufferedWriter(new FileWriter(file));
			for (String string:list) {
				writer.write(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		generateData("data/test.txt", 1000000);
	}
}
