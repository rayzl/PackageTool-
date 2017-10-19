package com.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.tool.constant.Constant;
import com.tool.task.TaskListener;

public class DeviceTool {
	private String fileName;
	private TaskListener taskListener;
	
	public String ApkTOpen(String apkPath) throws IOException{
		String fileName = apkPath.substring(apkPath.lastIndexOf(File.separator)+1, apkPath.lastIndexOf("."));
		outMsg("start apk to open");
		String outFilePath= Constant.PATH_OUT+File.separator+"smali"+File.separator+fileName;
		hasFileDelete(outFilePath);
		String executeStr = Constant.PATH_APKTOOL+File.separator+"apktool.bat d "+apkPath+" -o "+outFilePath;
		executor(executeStr);
		outMsg("end apk to open");
		outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
	}
	
	public String ApkTDex(String apkPath) throws ZipException, IOException{
		String fileName = apkPath.substring(apkPath.lastIndexOf(File.separator)+1, apkPath.lastIndexOf("."));
		outMsg("start apk to dex");
		System.out.println("fileName:"+fileName);
		String outFilePath= Constant.PATH_OUT+File.separator+"dex"+File.separator+fileName;
		hasFileDelete(outFilePath);
		File outParentPathDir = new File(outFilePath);
		if(!outParentPathDir.exists()){
			outParentPathDir.mkdirs();
		}
		File file = new File(apkPath);    
	    ZipFile zipFile = new ZipFile(file);    
	    Enumeration<? extends ZipEntry> files = zipFile.entries();    
	    ZipEntry entry = null;    
	    BufferedInputStream bin = null;    
	    BufferedOutputStream bout = null;    
	    while (files.hasMoreElements()) {   
			entry = (ZipEntry) files.nextElement();  
			String entryName = entry.getName();  
			String suffixStr = entryName.substring(entryName.lastIndexOf(".")+1, entryName.length());
			if (!suffixStr.contains("dex")) continue;
			File outFile = new File(outFilePath+File.separator+entryName);
			System.out.println(outFilePath+File.separator+entryName);
			outFile.createNewFile();
			bin = new BufferedInputStream(zipFile.getInputStream(entry));    
			bout = new BufferedOutputStream(new FileOutputStream(outFile));    
			byte[] buffer = new byte[1024];    
			int readCount = -1;    
			while ((readCount = bin.read(buffer)) != -1) {    
			    bout.write(buffer, 0, readCount);    
			} 
		    bout.flush();
		    bout.close();
	    }
	    outMsg("end apk to dex");
	    outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
	}
	
	public String ApkTJar(String apkPath) throws ZipException, IOException{
		outMsg("start apk to jar");
		String fileName = apkPath.substring(apkPath.lastIndexOf(File.separator)+1, apkPath.lastIndexOf("."));
		String outFilePath= Constant.PATH_OUT+File.separator+"jar"+File.separator+fileName;
		hasFileDelete(outFilePath);
		File outParentPathDir = new File(outFilePath);
		if(!outParentPathDir.exists()){
			outParentPathDir.mkdirs();
		}
		String dexs = ApkTDex(apkPath);
		File file = new File(dexs);
		File[] dexFiles = file.listFiles();
		for(File dexFile:dexFiles){
			DexTJar(dexFile.getAbsolutePath());
		}
		 outMsg("end apk to jar");
		 outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
	}
	
	public String DexTSmali(String dexPath) throws IOException{
		String fileName = dexPath.substring(dexPath.lastIndexOf(File.separator)+1, dexPath.lastIndexOf("."));
		outMsg("start dex to smali");
		String outFilePath= Constant.PATH_OUT+File.separator+"smali"+File.separator+fileName;
		hasFileDelete(outFilePath);
		File outParentPathDir = new File(outFilePath);
		if(!outParentPathDir.exists()){
			outParentPathDir.mkdirs();
		}
		String executeStr = "java -jar "+Constant.PATH_APKTOOL+File.separator+"baksmali.jar -o "+outFilePath+" "+dexPath;
		executor(executeStr);
		outMsg("end dex to smali");
		outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
	}
	
	public String DexTJar(String dexPath) throws IOException{
		String fileName = dexPath.substring(dexPath.lastIndexOf(File.separator)+1, dexPath.lastIndexOf("."));
		outMsg("start dex to jar");
		String outFilePath= Constant.PATH_OUT+File.separator+"jar"+File.separator+fileName;
		hasFileDelete(outFilePath);
		File outParentPathDir = new File(outFilePath);
		if(!outParentPathDir.exists()){
			outParentPathDir.mkdirs();
		}
		String executeStr = Constant.PATH_DEX2JAR+File.separator+"d2j-dex2jar.bat "+dexPath+" -o /"+outFilePath+File.separator+fileName+".jar";
		executor(executeStr);
		outMsg("end dex to jar");
		outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
	}
	
	public String JarTDex(String jarPath) throws IOException{
		System.out.println("jarPath:"+jarPath+"   files:"+File.separator);
		String fileName = jarPath.substring(jarPath.lastIndexOf(File.separator)+1, jarPath.lastIndexOf("."));
		outMsg("start jar to dex");
		String outFilePath= Constant.PATH_OUT+File.separator+"dex"+File.separator+fileName;
		hasFileDelete(outFilePath);
		File outParentPathDir = new File(outFilePath);
		if(!outParentPathDir.exists()){
			outParentPathDir.mkdirs();
		}
		String executeStr =  Constant.PATH_JARTDEX+File.separator+"dx.bat --dex --output="+new File(outFilePath+File.separator+fileName+".dex ").getAbsolutePath()+jarPath;
		executor(executeStr);
		outMsg("end jar to dex");
		outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
	}
	
	public String jarTSmali(String jarPath) throws IOException{
		String fileName = jarPath.substring(jarPath.lastIndexOf(File.separator)+1, jarPath.lastIndexOf("."));
		outMsg("start jar to smali");
		String outFilePath= Constant.PATH_OUT+File.separator+"smali"+File.separator+fileName;
		hasFileDelete(outFilePath);
		File outParentPathDir = new File(outFilePath);
		if(!outParentPathDir.exists()){
			outParentPathDir.mkdirs();
		}
		String dexPath = JarTDex(jarPath);
		String dexFilePath = dexPath+File.separator+fileName+".dex ";
		DexTSmali(dexFilePath);
		outMsg("end jar to smali");
		outMsg("outFilePath\n"+new File(outFilePath).getAbsolutePath());
		return outFilePath;
		
	}
	
	public void ApkObation(String apkPath) throws IOException{
		outMsg("start obation apk info");
		String executeStr = Constant.PATH_AAPT+File.separator+"aapt.exe "+"dump badging "+apkPath;
		executor(executeStr);
		outMsg("end obation apk info");
	}
	
	public void hasFileDelete(String path){
		File file = new File(path);
		if(!file.exists()) return;
		deleteFile(file);
	}
	
	//递归删除文件夹  
   private void deleteFile(File file) {  
	if (file.exists()) {//判断文件是否存在  
		if (file.isFile()) {//判断是否是文件  
			System.out.println("删除："+file.getName());
			file.delete();//删除文件   
		} else if (file.isDirectory()) {//否则如果它是一个目录  
			File[] files = file.listFiles();//声明目录下所有的文件 files[];  
			for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件  
				this.deleteFile(files[i]);//把每个文件用这个方法进行迭代  
			}  
			file.delete();//删除文件夹  
		}  
	}  
   }  
   
   public void executor(String executeStr) throws IOException{
	   	outMsg(executeStr);
		Process p = Runtime.getRuntime().exec(executeStr);
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));// 注意中文编码问题
		String line;
		while ((line = br.readLine()) != null) {
			outMsg(line);
		}
		
		if(line==null || line.equals("")){
			BufferedReader errorBr = new BufferedReader(new InputStreamReader(p.getErrorStream(), "GBK"));// 注意中文编码问题
			while ((line = errorBr.readLine()) != null) {
				outMsg(line);
			}
		}
		br.close();
   }

	public String getFileName() {
		return fileName;
	}

	public void setTaskListener(TaskListener taskListener) {
		this.taskListener = taskListener;
	}
   
	public void outMsg(String msg){
		if(taskListener != null){
			msg = fileName+":"+msg;
			taskListener.msg(msg);
		}else{
			System.out.println(msg);
		}
	}

	public void setFileName(String fileName) {
		this.fileName = fileName.substring(0, fileName.lastIndexOf("."));
	}
	
   
}
