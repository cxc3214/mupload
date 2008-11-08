package cn.mjc.mupload;

import java.io.File;

/**
 * 所有已被选中的文件列表.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class SelectedFile {
	private final static SelectedFile sf = new SelectedFile();
	private File selectedFile; // 被选择要上传的文件
	private File lastDir; // 上一次选择文件的路径信息记录，便于直接定位
	
	private SelectedFile() {
	}

	/**
	 * 获取被选择的文件列表保存对象的唯一实例.
	 * 
	 * @return
	 */
	public static SelectedFile getInstance() {
		return sf;
	}

	/**
	 * 使用此方法来设置要上传的文件.
	 * 
	 * @param f
	 */
	public void setSelectedFile(File f) {
		this.selectedFile = f;
	}

	/**
	 * 获取指定要上传的文件，针对只上传一个文件的时候.
	 * <P>
	 * 
	 * @return
	 */
	public File getSelectedFile() {
		return this.selectedFile;
	}

	/**
	 * 设置上一次选择文件的最后路径信息.
	 * 
	 * @param lastDir
	 */
	public void setLastDir(File lastDir) {
		this.lastDir = lastDir;
	}

	/**
	 * 获取最后一次打开文件的路径，如果为null则表示没有打开文件.
	 * 
	 * @return
	 */
	public File getLastDir() {
		return this.lastDir;
	}
}
