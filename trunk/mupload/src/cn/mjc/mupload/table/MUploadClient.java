package cn.mjc.mupload.table;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import cn.mjc.mupload.BaseMUploadClient;

/**
 * mupload文件上传控件客户端运行主类.
 * <P>
 * 
 * 该类采用TableLayout来进行GUI设计，同时设置可以显示上传进度、本地 要上传的文件、剩余时间与上传速度等显示.
 * 
 * @author 马必强
 * 
 */
public class MUploadClient extends BaseMUploadClient {
	private static final long serialVersionUID = -997140631402880497L;

	// 本地文件路径、预计上传时间、上传速度显示、上传提示的label
	private JLabel localFile, predictTime, uploadSpeed, uploadTips;

	/**
	 * applet客户端的页面组件初始化.
	 */
	public void init() {
		// 初始化参数
		initParameter();

		// GUI设置基本参数
		double p = TableLayout.PREFERRED, border = 15, emptySpace = 6;
		double[] columnSize = { border, 100, TableLayout.FILL, border };
		double[] rowSize = { border, border };
		TableLayout layout = new TableLayout(columnSize, rowSize);
		layout.setVGap(2);
		layout.setHGap(5);
		this.setLayout(layout);
		// 添加组件
		int rowIndex = 1;
		layout.insertRow(rowIndex++, p);
		this.add(uploadTips = new JLabel(), "1, 1, 2, 1");

		layout.insertRow(rowIndex++, emptySpace);
		layout.insertRow(rowIndex++, p);
		this.add(this.getProgressBar(), "1, 3, 2, 3");

		layout.insertRow(rowIndex++, emptySpace);
		layout.insertRow(rowIndex++, p);
		this.add(new JLabel("本地上传文件："), "1, 5, 1, 5");
		this.add(localFile = new JLabel(), "2, 5, 2, 5");

		layout.insertRow(rowIndex++, emptySpace);
		layout.insertRow(rowIndex++, p);
		this.add(new JLabel("估计剩余时间："), "1, 7, 1, 7");
		this.add(predictTime = new JLabel(), "2, 7, 2, 7");

		layout.insertRow(rowIndex++, emptySpace);
		layout.insertRow(rowIndex++, p);
		this.add(new JLabel("当前上传速度："), "1, 9, 1, 7");
		this.add(uploadSpeed = new JLabel(), "2, 9, 2, 9");

		layout.insertRow(rowIndex++, emptySpace);
		layout.insertRow(rowIndex++, p);
		this.add(this.getButtonArea(), "1, 11, 2, 11");

		// 初始化文件上传状态对象
		uploadStatus = new TableFileUploadStatus(progressBar, this.predictTime,
				this.uploadSpeed);
	}

	private JPanel getButtonArea() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(browserBtn = this.getBrowserButton());
		panel.add(uploadBtn = this.getUploadButton());
		return panel;
	}

	/**
	 * 将用户选择的文件的相关信息显示出来.
	 * 
	 * @param f
	 */
	protected void setFileInfo(File f) {
		this.uploadTips.setText("上传文件 " + f.getName() + " 到 "
				+ this.setting.getDocBase().getHost());
		String fsizeTxt; // 文件大小提示信息
		float msize = f.length() * 1f / (1024 * 1024);
		if (msize >= 1.0) {
			fsizeTxt = String.valueOf(msize);
			int point = fsizeTxt.indexOf(".");
			if (point > 0 && point + 3 < fsizeTxt.length()) {
				fsizeTxt = fsizeTxt.substring(0, point + 3) + " MB";
			}
		} else {
			fsizeTxt = ((f.length() / 1024) + (f.length() % 1024 > 0 ? 1 : 0))
					+ " KB";
		}
		fsizeTxt = "（文件大小：" + fsizeTxt + "）";
		this.localFile.setText(f.getName() + fsizeTxt);
		this.localFile.setToolTipText("文件路径：" + f.getPath() + fsizeTxt);
	}

	private JProgressBar getProgressBar() {
		this.progressBar = new JProgressBar(0, 1000);
		progressBar.setBackground(Color.WHITE);
		progressBar.setPreferredSize(new Dimension(this.getWidth(), 25));
		// progressBar.setVisible(false); // 初始不可见
		progressBar.setStringPainted(true); // 设置可以显示字符串
		return this.progressBar;
	}
}
