package com.sparrow.coding;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import com.sparrow.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 用于eclipseGBK创建后的utf-8转码问题
 * 
 * @author zhangizhi
 * 
 */
public class ConvertEncoding {
	static Logger logger = LoggerFactory.getLogger(ConvertEncoding.class);
	private static boolean isTest = true;
	private static String sourceEncoding = "gb2312";
	private static String descEncoding = "UTF-8";

	/**
	 * @author zlz
	 * 
	 * @time 2013-7-16上午10:26:40
	 * @param args
	 */
	public static void main(String[] args) {

		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String rootPath = null;
		String yn = null;
		System.out.println("请输入要转换的路径");
		try {
			rootPath = bf.readLine();
		} catch (IOException e) {
		}

		Convert(rootPath);
		System.out.println("请输入文件的当前编码,回车默认为gbk");
		try {
			String tempEncoding = bf.readLine();
			if (!"".equals(tempEncoding)) {
				sourceEncoding = bf.readLine();
			}
		} catch (IOException e) {
		}

		System.out.println("请输入目标文件编码，回车默认为utf-8");
		try {
			String tempEncoding = bf.readLine();
			if (!"".equals(tempEncoding)) {
				descEncoding = bf.readLine();
			}
		} catch (IOException e) {
		}

		System.out.println("立即生成吗？Y/N");

		try {
			yn = bf.readLine();
		} catch (IOException e) {
		}

		if (yn.toLowerCase().equals("y")) {
			isTest = false;
			Convert(rootPath);
			System.out.println("ending");
		}
	}

	public static void Convert(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isDirectory() && !f.isHidden()) {
				Convert(f.getPath());
			} else {
				String fileName = f.getPath();
				String sourceText = FileUtility.getInstance().readFileContent(
						fileName, sourceEncoding);
				if (fileName.endsWith(".java")) {
					if (isTest) {
						System.out.println(sourceText);
					} else {
						FileUtility.getInstance().writeFile(fileName, sourceText,
								descEncoding);
						System.out.println(fileName + " is encoded");
					}
				}
			}
		}
	}
}
