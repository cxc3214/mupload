package cn.mjc.mupload.upload;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.swing.JProgressBar;

import cn.mjc.mupload.StringUtil;

/**
 * 文件上传的状态对象基类，记录文件当前上传的字节数和总的字节数.
 * <P>
 * 
 * 同时该类继承了Observable类，可以应用观察者模式对其进行状态变更的查看.
 * 
 * 文件上传的状态有： WAITING_FOR_SELECT_FILE 等待选择文件 WAITING_FOR_UPLOAD 已选择文件，等待上传
 * FILE_UPLOADING 文件正在上传中 FILE_SIZE_TOO_LONG 文件的大小已超过最大上传的限制
 * FILE_SIZE_OVER_LIMIT 文件的大小已超过了指定的限制 FILE_UPLOAD_COMPLETE 文件上传完毕
 * FILE_UPLOAD_FAILED 文件上传失败
 * 
 * @author 马必强
 * 
 */
public abstract class BaseFileUploadStatus extends Observable {
	public static enum Status {
		WAITING_FOR_SELECT_FILE, WAITING_FOR_UPLOAD, FILE_UPLOADING, FILE_SIZE_TOO_LONG, FILE_SIZE_OVER_LIMIT, FILE_UPLOAD_COMPLETE, FILE_UPLOAD_FAILED
	} // 文件上传的状态

	protected static Map<Status, String> statusDesc; // 状态的描述信息

	private Status currentStatus = Status.WAITING_FOR_SELECT_FILE; // 当前的状态

	protected long totalSize; // 总的文件大小

	protected long uploadedSize; // 已上传了的大小

	protected Date startTime, endTime; // 文件开始上传和结束上传的时间

	protected JProgressBar progressBar; // 文件上传的进度条

	static {
		statusDesc = new HashMap<Status, String>();
		statusDesc.put(Status.WAITING_FOR_SELECT_FILE, "等待选择文件");
		statusDesc.put(Status.WAITING_FOR_UPLOAD, "等待上传文件");
		statusDesc.put(Status.FILE_UPLOADING, "文件上传中");
		statusDesc.put(Status.FILE_SIZE_TOO_LONG, "文件大小超出系统最大限制");
		statusDesc.put(Status.FILE_SIZE_OVER_LIMIT, "文件大小超出指定大小限制");
		statusDesc.put(Status.FILE_UPLOAD_COMPLETE, "文件上传成功");
		statusDesc.put(Status.FILE_UPLOAD_FAILED, ""); // 文件上传失败的信息由不同的上传程序进行设置
	}

	protected BaseFileUploadStatus(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	/**
	 * 获取某一指定状态的描述语言.
	 * 
	 * @return
	 */
	public static String getStatusDesc(Status stat) {
		String result = statusDesc.get(stat);
		if (result == null)
			return "未知状态";
		return result;
	}

	/**
	 * 设置文件上传的进度显示条.
	 * <P>
	 * 传递的参数为当前的进度百分比和要显示在进度条上的字符串，如果没有则不显示.
	 * 
	 * @return
	 */
	public void setProgress(float percent, String tips) {
		if (!this.progressBar.isVisible())
			this.progressBar.setVisible(true);
		this.progressBar
				.setValue(((Float) (this.progressBar.getMaximum() * percent))
						.intValue());
		if (StringUtil.trim(tips).intern() != "") {
			this.progressBar.setString(StringUtil.trim(tips));
		}
	}

	/**
	 * 设置文件开始上传.同时设置其上传的开始时间和状态、总的文件大小！
	 * 
	 * @param dt
	 */
	public void uploadStart(long totalSize) {
		this.startTime = Calendar.getInstance().getTime(); // 文件开始上传的时间
		this.currentStatus = Status.FILE_UPLOADING;
		this.totalSize = totalSize;
		// 重置已上传的字节数和上传结束的时间
		this.uploadedSize = 0;
		this.endTime = null;
	}

	/**
	 * 获取当前的文件上传状态.
	 * 
	 * @return
	 */
	public Status getCurrentStatus() {
		return this.currentStatus;
	}

	/**
	 * 设置文件上传失败的失败信息.
	 * 
	 * @param errorMsg
	 */
	public void setUploadFailed(String errorMsg) {
		this.currentStatus = Status.FILE_UPLOAD_FAILED;
		BaseFileUploadStatus.statusDesc
				.put(Status.FILE_UPLOAD_FAILED, errorMsg);
	}

	/**
	 * 设置当前的文件上传状态.
	 * 
	 * @param status
	 */
	public void setCurrentStatus(Status status) {
		this.currentStatus = status;
	}

	/**
	 * 获取总的需要上传的文件大小.单位为b
	 * 
	 * @return
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * 获取当前已经上传的文件大小，单位为b
	 * 
	 * @return
	 */
	public long getUploadedSize() {
		return uploadedSize;
	}

	/**
	 * 设置当前上传的大小，同时激活观察者.
	 * 
	 * @param size
	 */
	public void upload(long size) {
		this.uploadedSize += size;
		float per = 0f;
		if (this.totalSize > 0) {
			per = (this.uploadedSize * 1f) / this.totalSize;
		}
		this.setChanged();
		this.notifyObservers(per);
	}

	/**
	 * 文件上传成功！并返回总共耗费的时间.
	 * 
	 */
	public long uploadComplete() {
		// 设置状态为上传成功
		this.currentStatus = Status.FILE_UPLOAD_COMPLETE;
		// 获取总的上传时间
		this.endTime = Calendar.getInstance().getTime();
		long times = this.endTime.getTime() - this.startTime.getTime();
		this.uploadComplete(times);
		return times;
	}

	/**
	 * 获取总的上传时间，外部获取.如果没有上传完毕则返回null
	 * 
	 * @return
	 */
	public String getTotalTimes() {
		if (this.endTime == null)
			return null;
		return this.convertTimes(this.endTime.getTime()
				- this.startTime.getTime());
	}

	/**
	 * 获取上传速度的字符串表现形式,例如：43.5KB/s
	 * 
	 * @return
	 */
	public String getSpeedString() {
		String unit = " KB/s";
		String speed = String.valueOf(this.getSpeedFloat());
		int point = speed.indexOf(".");
		if (point == -1)
			return speed + unit;
		if (point + 2 >= speed.length())
			return speed + unit;
		return speed.substring(0, point + 2) + unit;
	}

	/**
	 * 获取当前文件上传的速度.
	 * 
	 * @return
	 */
	public float getSpeedFloat() {
		long times = System.currentTimeMillis() - this.startTime.getTime();
		if (times < 1000)
			times += 1000; // 如果小于1000ms则设置为1s
		return (this.uploadedSize * 1f / 1024) / (times / 1000);
	}

	/**
	 * 文件数据传输完毕,但还在等待服务器的结果返回.
	 * 
	 */
	public abstract void dataTransportComplete();

	/**
	 * 子类的文件上传成功的方法，传递的参数为消耗的时间.
	 * 
	 * @param times
	 */
	protected abstract void uploadComplete(long times);

	/**
	 * 将指定的时间转换成分秒的表示，如20s或5分10秒.
	 * 
	 * @return
	 */
	protected String convertTimes(long times) {
		if (times < 1000)
			return "1秒";
		int seconds = ((Long) (times / 1000)).intValue();
		if (seconds < 60)
			return seconds + "秒";
		int minutes = seconds / 60, tmp = seconds % 60;
		if (minutes < 60)
			return minutes + "分" + (tmp > 0 ? (tmp + "秒") : "");
		int hours = seconds / 3600;
		minutes = (seconds % 60) / 60;
		tmp = seconds % 3600;
		return hours + "小时" + (minutes > 0 ? (minutes + "分") : "")
				+ (tmp > 0 ? (tmp + "秒") : "");
	}

	/**
	 * 将指定的单位为b的文件大小转换成单位为kb或m的字符串表现形式.
	 * 
	 * @param size
	 * @return
	 */
	protected String convertSize(long size) {
		if (size < 1024)
			return size + "B";
		String fs = null, unit = "KB";
		if (size < 1024 * 1024)
			fs = String.valueOf(size * 1f / 1024);
		else {
			fs = String.valueOf(size * 1f / (1024 * 1024));
			unit = "MB";
		}
		int point = fs.indexOf(".");
		if (point == -1 || point >= fs.length() - 3)
			return fs + unit;
		return fs.substring(0, point + 2) + unit;
	}
}
