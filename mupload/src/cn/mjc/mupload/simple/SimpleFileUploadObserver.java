package cn.mjc.mupload.simple;

import java.util.Observable;
import java.util.Observer;

/**
 * 文件上传状态监控的观察者.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class SimpleFileUploadObserver implements Observer {
	/**
	 * 更新进度条.
	 * 
	 * @param arg0
	 *            FileUploadStatus的实例
	 * @param arg1
	 *            当前已上传的百分比,float型
	 */
	public void update(Observable arg0, Object arg1) {
		SimpleFileUploadStatus fus = (SimpleFileUploadStatus) arg0;
		fus.setProgress((Float) arg1, fus.getSpeedString());
	}

}
