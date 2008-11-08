package cn.mjc.mupload.simple;

import javax.swing.JProgressBar;

import cn.mjc.mupload.upload.BaseFileUploadStatus;

/**
 * 文件上传的状态对象，记录文件当前上传的字节数和总的字节数.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class SimpleFileUploadStatus extends BaseFileUploadStatus {
	public SimpleFileUploadStatus(JProgressBar progressBar) {
		super(progressBar);
		this.addObserver(new SimpleFileUploadObserver()); // 添加一个观察者
	}

	/**
	 * 文件上传成功！并返回总共耗费的时间.
	 * 
	 */
	@Override
	protected void uploadComplete(long times) {
		this.progressBar.setString("文件上传成功！总共耗时" + this.convertTimes(times));
	}

	@Override
	public void dataTransportComplete() {

	}
}
