package com.tool.convert.Type;

public enum ConvertType {
	APKTJAR("APkToJar"),
	APKTOPEN("ApkToOpen"),
	APKTOBATION("apkObation"),
	DEXTSMALI("DexToSmali"),
	DEXTJAR("DexToJar"),
	JARTDEX("JarToDex"),
	JARTOSMALI("JarToSamli");
	public String convertName;

	private ConvertType(String convertName) {
		this.convertName = convertName;
	}

	public String getConvertName() {
		return convertName;
	}
	
	
}
