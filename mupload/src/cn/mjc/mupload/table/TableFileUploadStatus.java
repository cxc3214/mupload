package cn.mjc.mupload.table;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import cn.mjc.mupload.upload.BaseFileUploadStatus;

/**
 * 文件上传的状态对象，记录文件当前上传的字节数和总的字节数.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class TableFileUploadStatus extends BaseFileUploadStatus {
	private JLabel predictTime, uploadSpeed; // 估计剩余时间与上传速度显示

	public TableFileUploadStatus(JProgressBar progressBar, JLabel predictTime,
			JLabel uploadSpeed) {
		super(progressBar);
		this.predictTime = predictTime;
		this.uploadSpeed = uploadSpeed;
		this.addObserver(new TableFileUploadObserver()); // 添加一个观察者
	}

	/**
	 * 文件上传成功！并返回总共耗费的时间.
	 * 
	 */
	@Override
	public void uploadComplete(long times) {
		predictTime.setText("文件上传成功！");
		uploadSpeed.setText("已上传 " + this.convertSize(this.uploadedSize)
				+ "，总共 " + this.convertSize(this.totalSize));
	}

	/**
	 * 设置还剩余的上传时间.
	 * 
	 * @param times
	 *            单位为毫秒
	 */
	public void setPredictTime(long times) {
		this.predictTime.setText(this.convertTimes(times));
	}

	/**
	 * 设置文件上传的当前速度和文件总量的信息
	 */
	public void setUploadSpeed() {
		this.uploadSpeed.setText(this.getSpeedString() + " （已上传："
				+ this.convertSize(this.uploadedSize) + "，总共："
				+ this.convertSize(this.totalSize) + "）");
	}

	@Override
	public void dataTransportComplete() {
		this.predictTime.setText("处理中，请稍等......");
	}
}
