package cn.mjc.mupload.table;

import java.util.Observable;
import java.util.Observer;

/**
 * 文件上传状态监控的观察者.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class TableFileUploadObserver implements Observer {
	/**
	 * 更新进度条.
	 * 
	 * @param arg0
	 *            TableFileUploadStatus的实例
	 * @param arg1
	 *            当前已上传的百分比,float型
	 */
	public void update(Observable arg0, Object arg1) {
		TableFileUploadStatus fus = (TableFileUploadStatus) arg0;
		fus.setProgress((Float) arg1, null);
		// 更新估计剩余上传时间
		float speed = fus.getSpeedFloat();
		long remainSize = fus.getTotalSize() - fus.getUploadedSize();
		int times = ((Float) ((remainSize / 1024) / speed)).intValue();
		fus.setPredictTime(times * 1000);
		// 设置上传速度信息
		fus.setUploadSpeed();
	}

}
