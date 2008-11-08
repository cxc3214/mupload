package cn.mjc.mupload;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import netscape.javascript.JSObject;
import cn.mjc.mupload.table.TableFileUploadStatus;
import cn.mjc.mupload.upload.AbstractUpload;
import cn.mjc.mupload.upload.BaseFileUploadStatus;
import cn.mjc.mupload.upload.BaseFileUploadStatus.Status;

/**
 * 不同样式布局mupload客户端的基类.
 * 
 * @author 马必强
 * 
 */
public abstract class BaseMUploadClient extends JApplet {
	private static final long serialVersionUID = 2284215168536281219L;
	// 最大的上传文件大小为1.5G
	protected final static int MAX_UPLOAD_SIZE = (int) (1.5 * 1024 * 1024 * 1024);
	protected MUploadSetting setting; // 上传参数设置对象
	protected BaseFileUploadStatus uploadStatus; // 文件上传的状态对象
	protected JProgressBar progressBar; // 文件的上传进度显示组件
	protected JButton browserBtn, uploadBtn; // 文件浏览和文件上传的按钮

	/**
	 * 获取当前文件上传的状态，方便与JS通信.
	 * <P>
	 * 系统返回的是一js对象形式的字符串，格式为：{status : WAITING_FOR_SELECT_FILE, message :
	 * '等待选择文件'}
	 * 
	 * @return
	 */
	public String getStatus() {
		Status stat = uploadStatus.getCurrentStatus();
		return String.format("{status:\"%s\",message:\"%s\"}", stat.toString(),
				BaseFileUploadStatus.getStatusDesc(stat));
	}

	/**
	 * 获取文件浏览的按钮.
	 * 
	 * @return
	 */
	protected JButton getBrowserButton() {
		JButton btn = new JButton("浏览文件");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// 打开文件选择框
				JFileChooser jfc = new JFileChooser(SelectedFile.getInstance()
						.getLastDir());
				List<FileTypes> ftypes = setting.parseLimitTypes();
				if (ftypes != null && ftypes.size() > 0) {
					jfc.setAcceptAllFileFilterUsed(false);
					for (final FileTypes ft : ftypes) {
						jfc.addChoosableFileFilter(new FileFilter() {
							@Override
							public boolean accept(File f) {
								if (f == null)
									return false;
								if (f.isDirectory())
									return true;
								if (StringUtil.includeIgnoreCase(ft.getExt(),
										this.getExtension(f)))
									return true;
								return false;
							}

							@Override
							public String getDescription() {
								return ft.getDesc();
							}

							/**
							 * 获取文件的后缀.
							 */
							protected String getExtension(File f) {
								String filename = f.getName();
								int i = filename.lastIndexOf('.');
								if (i > 0 && i < filename.length() - 1) {
									return filename.substring(i + 1);
								}
								return null;
							}
						});
					}
				}
				int result = jfc.showOpenDialog(null);
				// 选择文件后将文件的相关信息进行展示
				if (result == JFileChooser.APPROVE_OPTION) {
					File sf = jfc.getSelectedFile();
					SelectedFile.getInstance().setSelectedFile(sf);
					SelectedFile.getInstance().setLastDir(
							jfc.getCurrentDirectory());
					setFileInfo(sf);
					// 如果文件的大小超过了限制(分系统的最大值和用户指定的最大值)则设置状态为超出限制，否则设置为等待上传
					if (sf.length() > MAX_UPLOAD_SIZE) {
						uploadStatus
								.setCurrentStatus(TableFileUploadStatus.Status.FILE_SIZE_TOO_LONG);
					} else if (setting.getLimitSize() > 0L
							&& (sf.length() / 1024) > setting.getLimitSize()) {
						uploadStatus
								.setCurrentStatus(TableFileUploadStatus.Status.FILE_SIZE_OVER_LIMIT);
					} else {
						uploadStatus
								.setCurrentStatus(TableFileUploadStatus.Status.WAITING_FOR_UPLOAD);
						// 成功选择文件后,回调方法
						onSelectCallback();
					}
				}
			}
		});
		return btn;
	}

	/**
	 * 获取文件上传的按钮.
	 * 
	 * @return
	 */
	protected JButton getUploadButton() {
		JButton btn = new JButton("上传文件");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// 如果没有选择文件则进行提示
				if (uploadStatus.getCurrentStatus() == BaseFileUploadStatus.Status.WAITING_FOR_SELECT_FILE
						|| SelectedFile.getInstance().getSelectedFile() == null) {
					JOptionPane.showMessageDialog(null, "请先选择要上传的文件！",
							"文件上传提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				// 如果文件的大小已经超过了系统的最大值则不允许上传
				if (uploadStatus.getCurrentStatus() == BaseFileUploadStatus.Status.FILE_SIZE_TOO_LONG) {
					JOptionPane.showMessageDialog(null, "您选择的文件大小已经超过了系统最大"
							+ (MAX_UPLOAD_SIZE / 1024) + "KB的限制！", "文件上传提示",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (uploadStatus.getCurrentStatus() == BaseFileUploadStatus.Status.FILE_SIZE_OVER_LIMIT) {
					// 如果限制了文件大小，且当前选择的文件大小已经超过限制则进行提示
					JOptionPane.showMessageDialog(null, "您选择的文件大小已经超过了"
							+ setting.getLimitSize() + "KB的限制！", "文件上传提示",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// 使文件浏览按钮和文件上传按钮不可用
				browserBtn.setEnabled(false);
				uploadBtn.setEnabled(false);
				// 文件上传开始
				new Thread(new FileUploader()).start();
			}
		});
		return btn;
	}

	/**
	 * 初始化参数设置.
	 * 
	 */
	protected void initParameter() {
		this.setting = MUploadSetting.getInstance();
		this.setting.setLimitTypes(this.getParameter("limitTypes"));
		this.setting.setLimitSize(StringUtil.parseLong(this
				.getParameter("limitSize"), -1L));
		this.setting.setUploadType(this.getParameter("uploadType"));
		this.setting.setOnComplete(this.getParameter("onComplete"));
		this.setting.setOnFailed(this.getParameter("onFailed"));
		this.setting.setOnSelect(this.getParameter("onSelect"));
		this.setting.setHttpURL(this.getParameter("httpURL"));
		this.setting.setDocBase(this.getDocumentBase());
	}

	/**
	 * 文件上传成功后调用JS的回调方法，如果有指定的话.没有指定则使用默认的提示框进行提示。
	 * 
	 * @param serverFilePath
	 *            文件上传后的路径
	 */
	protected boolean onCompleteCallback(String serverFilePath) {
		if (StringUtil.trim(setting.getOnComplete()).equals(""))
			return false;
		JSObject win = JSObject.getWindow(this);
		File f = SelectedFile.getInstance().getSelectedFile();
		win.call(setting.getOnComplete(), new Object[] { f.getPath(),
				f.length(), serverFilePath });
		return true;
	}

	/**
	 * 文件上传失败后的回调方法.参数为选择文件的路径和大小(单位字节)
	 */
	protected void onFailedCallback(String errorMsg) {
		if (StringUtil.trim(setting.getOnFailed()).equals(""))
			return;
		JSObject win = JSObject.getWindow(this);
		File f = SelectedFile.getInstance().getSelectedFile();
		win.call(setting.getOnFailed(), new Object[] { f.getPath(), f.length(),
				errorMsg });
	}

	/**
	 * 文件成功选择后的回调方法.传递的参数为文件路径和文件大小(单位字节)
	 */
	protected void onSelectCallback() {
		if (StringUtil.trim(setting.getOnSelect()).equals(""))
			return;
		JSObject win = JSObject.getWindow(this);
		File f = SelectedFile.getInstance().getSelectedFile();
		win.call(setting.getOnSelect(),
				new Object[] { f.getPath(), f.length() });
	}

	/**
	 * 设置选择的文件的信息.
	 * 
	 * @param f
	 */
	protected abstract void setFileInfo(File f);

	/**
	 * 文件上传.
	 * 
	 */
	private class FileUploader implements Runnable {
		public void run() {
			try {
				AbstractUpload uploader = AbstractUpload.getUploader(setting,
						uploadStatus);
				String returnVal = StringUtil.trim(uploader.startUpload());
				// 判断是否上传成功,只有格式满足:true=上传后的路径 才表示成功
				if (returnVal.equals("true=") || !returnVal.startsWith("true=")) {
					onFailedCallback(returnVal);
				} else if (!onCompleteCallback(returnVal.substring(5))) {
					JOptionPane.showMessageDialog(null, "文件上传成功！总共耗时"
							+ uploadStatus.getTotalTimes(), "文件上传提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception ex) {
				uploadStatus.setUploadFailed(ex.getLocalizedMessage());
				onFailedCallback(ex.getMessage());
			}
		}
	}
}
