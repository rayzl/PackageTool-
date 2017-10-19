package com.main;

import java.io.File;
import java.io.IOException;

import com.tool.DeviceTool;
import com.tool.layout.ToolLayout;


public class Main {
	public static void main(String[] args) {
//		ToolLayout toolLayout = new ToolLayout();
//		toolLayout.showJFrame();
		DeviceTool deviceTool = new DeviceTool();
		try {
//			"+File.separator+"
			deviceTool.ApkTJar("E:"+File.separator+"download"+File.separator+"360"+File.separator+"mfwz (5).apk");
			deviceTool.DexTJar("F:"+File.separator+"demo"+File.separator+"androideclipse"+File.separator+"PackageTool"+File.separator+"out"+File.separator+"dex"+File.separator+"mfwz"+File.separator+"classes.dex");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
