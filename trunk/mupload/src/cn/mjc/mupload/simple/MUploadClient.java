package cn.mjc.mupload.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import cn.mjc.mupload.BaseMUploadClient;

/**
 * mupload上传控件的applet客户端.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class MUploadClient extends BaseMUploadClient {
	private static final long serialVersionUID = 3937897143005199509L;

	private JLabel fnameLabel, fsizeLabel; // 文件的名称和大小标签对象

	/**
	 * applet客户端的页面组件初始化.
	 */
	@Override
	public void init() {
		// 初始化参数
		this.initParameter();

		// 初始化面板设置
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		this.setContentPane(content);

		// 添加中间的进度条等区域
		content.add(this.getCenterPanel(), BorderLayout.CENTER);

		// 初始化底部的按钮区域
		content.add(this.getBottomPanel(), BorderLayout.SOUTH);

		// 初始化文件的状态
		uploadStatus = new SimpleFileUploadStatus(progressBar);
	}

	/**
	 * 底部操作按钮区域.
	 * 
	 * @return
	 */
	private JPanel getBottomPanel() {
		JPanel tmpPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(5);
		flowLayout.setVgap(10);
		tmpPanel.setLayout(flowLayout);
		// 添加按钮
		tmpPanel.add(browserBtn = this.getBrowserButton());
		tmpPanel.add(uploadBtn = this.getUploadButton());
		return tmpPanel;
	}

	/**
	 * 文件显示区域和进度条显示区域的容器设置.
	 * 
	 * @return
	 */
	private JPanel getCenterPanel() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new GridLayout(3, 1));
		fnameLabel = new JLabel("  文件名称：");
		tmp.add(fnameLabel);
		fsizeLabel = new JLabel("  文件大小：");
		tmp.add(fsizeLabel);
		// 进度条显示
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new FlowLayout());
		progressPanel.add(this.getProgressBar());
		tmp.add(progressPanel);
		return tmp;
	}

	/**
	 * 将用户选择的文件的相关信息显示出来.
	 * 
	 * @param f
	 */
	protected void setFileInfo(File f) {
		fnameLabel.setText("  文件名称：" + f.getName());
		fnameLabel.setToolTipText(f.getPath());
		String fsizeTxt; // 文件大小提示信息
		float msize = f.length() * 1f / (1024 * 1024);
		if (msize >= 1.0) {
			fsizeTxt = String.valueOf(msize);
			int point = fsizeTxt.indexOf(".");
			if (point > 0 && point + 3 < fsizeTxt.length()) {
				fsizeTxt = fsizeTxt.substring(0, point + 3);
			}
			fsizeLabel.setText("  文件大小：" + fsizeTxt + " MB");
			return;
		}
		fsizeLabel.setText("  文件大小："
				+ ((f.length() / 1024) + (f.length() % 1024 > 0 ? 1 : 0))
				+ " KB");
	}

	/**
	 * 添加一个进度条.
	 * 
	 * @return
	 */
	private JProgressBar getProgressBar() {
		progressBar = new JProgressBar(0, 1000);
		progressBar.setBackground(Color.WHITE);
		// progressBar.setForeground(Color.CYAN);
		progressBar.setPreferredSize(new Dimension(this.getWidth(), 25));
		progressBar.setVisible(false); // 初始不可见
		progressBar.setStringPainted(true); // 设置可以显示字符串
		return progressBar;
	}
}
