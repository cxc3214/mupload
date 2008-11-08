package cn.mjc.mupload;

/**
 * 要限制上传的文件类型域对象.
 * <P>
 * 
 * @author 马必强
 * 
 */
public class FileTypes {
	private String desc; // 文件类型的描述字符串
	private String[] ext; // 该类型的文件的后缀列表，如txt和jpg等

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String[] getExt() {
		return ext;
	}

	public void setExt(String[] ext) {
		this.ext = ext;
	}
}
