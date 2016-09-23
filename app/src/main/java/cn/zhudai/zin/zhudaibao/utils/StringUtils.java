package cn.zhudai.zin.zhudaibao.utils;

public class StringUtils {
	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
	public static String[] split(String org,String split){

		String[] sourceStrArray = org.split(split);

		return  sourceStrArray;
	}
	/**
	 * 保留两位小数，但是当小数为0时则不显示*/
	public static String leftoutZero(String dataS){
		/*String result="";
		double dataD=Double.valueOf(dataS);
		int dataI=(int)dataD;
		double smallNum=dataD-dataI;//小数部分
		if (smallNum!=0){
			result=dataS;
		}else {
			result=dataI+"";
		}
		return result;*/
		double dataD=Double.valueOf(dataS);
		if(dataD % 1.0 != 0){
			return dataS;
		}else {
			return String.valueOf((int)dataD);
		}

	}
	public static String leftoutZero(double dataD){
		if(dataD % 1.0 != 0){
			return dataD+"";
		}else {
			return String.valueOf((int)dataD);
		}

	}
	public static String Unicode2Chz(String utfString){
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while((i=utfString.indexOf("\\u", pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
			}
		}

		return sb.toString();
	}
}
