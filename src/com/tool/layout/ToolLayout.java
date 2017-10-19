package com.tool.layout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;  
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import javax.swing.*;  
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tool.DeviceTool;
import com.tool.convert.Type.ConvertType;
import com.tool.task.TaskListener;
import com.tool.task.TaskManager;

public class ToolLayout extends JFrame implements ActionListener,TaskListener{
	private final int FRAME_W = 300;
	private final int FRAME_H = 500;
	private JFileChooser chooseFile;
	private JLabel chooseFileLable;
	private JButton chooseFile_bt;
	
	
	private ButtonGroup operationBG;
	private JRadioButton jApktoJar;
	private JRadioButton jApktoOpen;
	private JRadioButton JApkObtion;
	
	private JRadioButton jDexToSmali;
	private JRadioButton JDexToJar;
	
	private JRadioButton jJarToSmali;
	private JRadioButton JJarToDex;
	
	
	private JButton startBt;
	private JButton openOutFile;
	JPanel chooseFileJP,operationJp,startJp,promptJp;//面板  
	private JScrollPane promptMsg;
	private JTextArea promptJT;
	public ToolLayout(){
		init();
	}
	
	public void init(){
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);//网格式布局  
		chooseFileJP = new JPanel();
		operationJp = new JPanel();
		promptJp = new JPanel();
		operationJp.setLayout(new GridLayout(3,3));
		startJp = new JPanel();
		initChooseFileBt();
		initChooseFileLable();
		
		initOperation();
		
		initOpenFileBt();
		initStartBt();
		
		initPromptMsg();
		this.add(chooseFileJP);
		this.add(operationJp);
		this.add(startJp);
		this.add(promptJp);
		initLayout(layout);
		
		TaskManager.getInstance().init(5, 10, 2, TimeUnit.SECONDS);
	}
    public void showJFrame(){
    	 //设置窗体  
        this.setTitle("android工具");//窗体标签  
        this.setSize(FRAME_W, FRAME_H);//窗体大小  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame  
        this.setLocationRelativeTo(null);
        //锁定窗体  
        this.setResizable(false);  
        this.setVisible(true);
    }
    
    public void showFileChoose(){
    	chooseFile = new JFileChooser(".");
    	chooseFile.setApproveButtonText("确定");  
    	int returnVal = chooseFile.showOpenDialog(this);
    	if (returnVal == JFileChooser.APPROVE_OPTION) {
    		chooseFileLable.setText(chooseFile.getSelectedFile().getName());
    		chooseFileJP.add(chooseFileLable);
    		chooseFileJP.updateUI();
    	}else{
    		chooseFileJP.remove(chooseFileLable);
    		chooseFileJP.updateUI();
    	}
    }
    
    public void initChooseFileBt(){
    	chooseFile_bt = new JButton("选择文件");
    	chooseFile_bt.addActionListener(this);
    	chooseFileJP.add(chooseFile_bt);
    }
    
    public void initChooseFileLable(){
    	chooseFileLable = new JLabel();
    }

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == chooseFile_bt){
			showFileChoose();
		}else if(e.getSource() == startBt){
			startExecute();
		}else if(e.getSource() == openOutFile){
			try {
				java.awt.Desktop.getDesktop().open(new File("out"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	private void startExecute() {
		String chooseFileName = chooseFile.getSelectedFile().getName();
		final String chooseFilePath = chooseFile.getSelectedFile().getPath();
		showPromptMsg("System:"+chooseFileName);
		if(!(chooseFileName.indexOf(".jar")>0 || chooseFileName.indexOf(".dex")>0 || chooseFileName.indexOf(".apk")>0)){
			showPromptMsg("System:"+"格式异常,暂时不能处理该类格式文件");
			return;
		}
		
		final DeviceTool deviceTool = new DeviceTool();
		deviceTool.setTaskListener(this);
		deviceTool.setFileName(chooseFileName);
		if(jApktoJar.isSelected()){
			if(chooseFileName.indexOf(".apk")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			TaskManager.getInstance().addTask(new Runnable() {
				
				public void run() {
					try {
						deviceTool.ApkTJar(chooseFilePath);
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}else if(jApktoOpen.isSelected()){
			if(chooseFileName.indexOf(".apk")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			TaskManager.getInstance().addTask(new Runnable() {
				
				public void run() {
					try {
						deviceTool.ApkTOpen(chooseFilePath);
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}else if(JApkObtion.isSelected()){
			if(chooseFileName.indexOf(".apk")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			TaskManager.getInstance().addTask(new Runnable() {
				
				public void run() {
					try {
						deviceTool.ApkObation(chooseFilePath);
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}else if(jDexToSmali.isSelected()){
			if(chooseFileName.indexOf(".dex")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			
			TaskManager.getInstance().addTask(new Runnable() {
				public void run() {
					try {
						deviceTool.DexTSmali(chooseFilePath);
					}catch (Exception e) {
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}else if(JDexToJar.isSelected()){
			System.out.println("jdexToJar:");
			if(chooseFileName.indexOf(".dex")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			TaskManager.getInstance().addTask(new Runnable() {
				public void run() {
					try {
						deviceTool.DexTJar(chooseFilePath);
					}catch (Exception e) {
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}else if(jJarToSmali.isSelected()){
			if(chooseFileName.indexOf(".jar")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			TaskManager.getInstance().addTask(new Runnable() {
				public void run() {
					try {
						deviceTool.jarTSmali(chooseFilePath);
					}catch (Exception e) {
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}else if(JJarToDex.isSelected()){
			if(chooseFileName.indexOf(".jar")==-1){
				showPromptMsg("System:"+"这种方式处理不了该文件哦");
				return;
			}
			TaskManager.getInstance().addTask(new Runnable() {
				public void run() {
					try {
						deviceTool.JarTDex(chooseFilePath);
					}catch (Exception e) {
						e.printStackTrace();
						showPromptMsg(deviceTool.getFileName()+":"+"异常了，下面是异常消息");
						showPromptMsg(deviceTool.getFileName()+":"+e.getMessage());
					}
				}
			});
		}
	}

	public void initOperation(){
		JLabel jLabel = new JLabel();
		jLabel.setText("选择操作项：");
		operationJp.add(jLabel);
		operationBG = new ButtonGroup(); 
		jApktoJar = new JRadioButton(ConvertType.APKTJAR.getConvertName());
		jApktoOpen = new JRadioButton(ConvertType.APKTOPEN.getConvertName());
		JApkObtion = new JRadioButton(ConvertType.APKTOBATION.getConvertName());
		jDexToSmali = new JRadioButton(ConvertType.DEXTSMALI.getConvertName());
		JDexToJar = new JRadioButton(ConvertType.DEXTJAR.getConvertName());
		jJarToSmali = new JRadioButton(ConvertType.JARTOSMALI.getConvertName());
		JJarToDex = new JRadioButton(ConvertType.JARTDEX.getConvertName());
		
		operationBG.add(jApktoJar);
		operationBG.add(jApktoOpen);
		operationBG.add(JApkObtion);
		
		operationBG.add(jDexToSmali);
		operationBG.add(JDexToJar);
		
		operationBG.add(jJarToSmali);
		operationBG.add(JJarToDex);
		
		
		operationJp.add(jApktoJar);
		operationJp.add(jApktoOpen);
		operationJp.add(JApkObtion);
		operationJp.add(jDexToSmali);
		operationJp.add(JDexToJar);
		operationJp.add(jJarToSmali);
		operationJp.add(JJarToDex);
		jApktoJar.setSelected(true);
	}
	
	public void initStartBt(){
		startBt = new JButton("执  行");
		startBt.addActionListener(this);
		startJp.add(startBt);
	}
	
	public void initOpenFileBt(){
		openOutFile = new JButton("打开输出文件夹");
		openOutFile.addActionListener(this);
		startJp.add(openOutFile);
	}
	
	public void initLayout(GridBagLayout layout){
		GridBagConstraints s= new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		s.gridwidth = 0;
		s.gridheight = 1;
		s.weightx = 0;
		s.weighty = 0;
		layout.setConstraints(chooseFileJP, s);
		
		GridBagConstraints s2= new GridBagConstraints();
		s2.fill = GridBagConstraints.BOTH;
		s2.gridwidth = 0;
		s2.gridheight = 2;
		s2.weightx = 0;
		s2.weighty = 1;
		layout.setConstraints(operationJp, s2);
		
		GridBagConstraints s3= new GridBagConstraints();
		s3.fill = GridBagConstraints.BOTH;
		s3.gridwidth = 0;
		s3.gridheight = 1;
		s3.weightx = 0;
		s3.weighty = 0;
		layout.setConstraints(startJp, s3);
		
		GridBagConstraints s4= new GridBagConstraints();
		s4.fill = GridBagConstraints.BOTH;
		s4.gridwidth = 0;
		s4.gridheight = 5;
		s4.weightx = 2;
		s4.weighty = 3;
		layout.setConstraints(promptJp, s4);
	}
	
	public void initPromptMsg(){
		promptMsg = new JScrollPane();
		promptJp.setLayout(new BorderLayout(0,0));
		promptJp.add(promptMsg,BorderLayout.CENTER);
		promptJT = new JTextArea();
		promptMsg.setViewportView(promptJT);  
		promptMsg.setBackground(Color.white);
	}
	
	public void showPromptMsg(String msg){
		promptJT.append(msg);
		promptJT.append("\n");
	}

	public void msg(String msg) {
		showPromptMsg(msg);
	}
	
	
}
