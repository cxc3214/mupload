package cn.mjc.mupload.upload;

import java.io.File;

import cn.mjc.mupload.MUploadSetting;
import cn.mjc.mupload.SelectedFile;

/**
 * 所有不同方式进行文件上传的抽象基础类.
 * <P>
 * 
 * @author 马必强
 * 
 */
public abstract class AbstractUpload {
	protected MUploadSetting setting; // 文件上传的设置信息类
	private BaseFileUploadStatus uploadStatus; // 文件上传的进度状态类
	private File selectedFile; // 当前已选择要上传的文件

	protected AbstractUpload(BaseFileUploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
		this.setting = MUploadSetting.getInstance();
		this.selectedFile = SelectedFile.getInstance().getSelectedFile();
		if (this.selectedFile == null || !this.selectedFile.isFile()) {
			throw new RuntimeException("没有指定要上传的文件或指定的文件不存在");
		}

	}

	/**
	 * 开始上传单独指定的文件的抽象方法.文件上传成功后的返回值为：文件上传后的路径信息
	 * 
	 */
	protected abstract String upload(File file,
			BaseFileUploadStatus uploadStatus) throws Exception;

	/**
	 * 开始文件上传的总入口方法.上传成功则返回上传到服务器上后文件路径,失败则返回null
	 * 
	 */
	public String startUpload() throws Exception {
		this.uploadStatus.uploadStart(this.selectedFile.length());
		String completeReturns = this.upload(this.selectedFile,
				this.uploadStatus);
		this.uploadStatus.uploadComplete();
		return completeReturns;
	}

	/**
	 * 根据设置获取不同的上传类.
	 * 
	 * @param setting
	 * @return
	 */
	public static AbstractUpload getUploader(MUploadSetting setting,
			BaseFileUploadStatus uploadStatus) {
		return new HttpUpload(uploadStatus);
	}
}
