package com.sparrow.coding;

import com.sparrow.coding.support.utils.PinyinUtil;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.HttpClient;

public class CheckDomain {

	public static void main(String[] args) {
		String fileName = EnvironmentSupport.getInstance()
				.getClassesPhysicPath() + "/搜狗标准词库.txt";

		String sougouWord = FileUtility.getInstance().readFileContent(fileName);
		String word[] = sougouWord.split("\n");
		int count = 0;
		for (String w : word) {
			if (w.length() <= 3) {
				count++;
				String pinyin = PinyinUtil.getPinYin(w).trim();
				String checkUrl = "http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=www."
						+ pinyin.trim() + ".com";
				String checkResult = HttpClient.get(checkUrl);
				if (checkResult.indexOf("211 : Domain name is not available") < 0) {
					//System.out
					//		.println(pinyin
					//				+ "$$$$$$$$$$$$$$$$$$$$$$$$$$yes$$$$$$$$$$$$$$$$$$$$$$$$$$");
				} else {
					System.out.println(pinyin
							+ "*******************no*********************");
				}
			}
		}
		System.out.println(count);
	}
}
