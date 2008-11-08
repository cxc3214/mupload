package cn.mjc.mupload;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * MUpload控件的参数设置域对象.
 * <P>
 * 
 * limitTypes 限制要上传的文件类型，格式为：文件描述=文件后缀列表&文件描述=文件后缀列表
 * 如：视频文件=avi,mp4,wmv&图片=jpg,png limitSize 要限制的文件大小，为-1则表示不进行限制，默认是-1.单位为KB
 * uploadType 文件上传的类型，为ftp和http-form，默认是http-form
 * 
 * @author 马必强
 * 
 */
public class MUploadSetting {
	private final static MUploadSetting instance = new MUploadSetting();
	private String limitTypes; // 限制上传的文件类型
	private long limitSize = -1; // 限制的文件大小，单位为KB -1表示不限制
	private String uploadType = "http-form"; // 文件上传类型 ftp、http-form(默认)
	private String onComplete, onFailed; // 文件上传成功和失败的回调方法.
	private String onSelect; // 成功选择文件后的回调方法
	private String httpURL; // http上传时的接收数据的URL地址
	private URL docBase; // applet嵌入的页面的URL信息

	private MUploadSetting() {
	}

	/**
	 * 获取设置对象的唯一实例.
	 * 
	 * @return
	 */
	public static MUploadSetting getInstance() {
		return instance;
	}

	public long getLimitSize() {
		return limitSize;
	}

	public void setLimitSize(long limitSize) {
		this.limitSize = limitSize;
	}

	public String getLimitTypes() {
		return limitTypes;
	}

	public void setLimitTypes(String limitTypes) {
		this.limitTypes = StringUtil.trim(limitTypes);
	}

	/**
	 * 返回文件的上传类型，ftp、http-form
	 * 
	 * @return
	 */
	public String getUploadType() {
		return this.uploadType;
	}

	public void setUploadType(String uploadType) {
		String tmp = StringUtil.trim(uploadType);
		if (tmp.equalsIgnoreCase("ftp"))
			this.uploadType = tmp;
	}

	/**
	 * 解析限制的文件上传类型格式字符串.
	 * <P>
	 * 如果传递过来的参数为空或是为false，则表示是不进行限制.该方法返回null则 表示是没有需要进行限制的文件类型列表.
	 * 
	 * @return
	 */
	public List<FileTypes> parseLimitTypes() {
		if (this.limitTypes == null || this.limitTypes.equals(""))
			return null;
		if (this.limitTypes.equalsIgnoreCase("false"))
			return null;
		String[] types = this.limitTypes.split("&");
		List<FileTypes> result = new ArrayList<FileTypes>();
		for (int i = 0; i < types.length; i++) {
			String[] tmp = types[i].trim().split("=");
			if (tmp.length != 2)
				continue;
			FileTypes ft = new FileTypes();
			ft.setDesc(StringUtil.trim(tmp[0]));
			ft.setExt(StringUtil.trim(tmp[1].split(",")));
			result.add(ft);
		}
		return result;
	}

	public String getHttpURL() {
		return httpURL;
	}

	public void setHttpURL(String httpURL) {
		this.httpURL = StringUtil.trim(httpURL);
	}

	public String getOnComplete() {
		return onComplete;
	}

	public void setOnComplete(String onComplete) {
		this.onComplete = StringUtil.trim(onComplete);
	}

	public String getOnFailed() {
		return onFailed;
	}

	public void setOnFailed(String onFailed) {
		this.onFailed = StringUtil.trim(onFailed);
	}

	public URL getDocBase() {
		return docBase;
	}

	public void setDocBase(URL docBase) {
		this.docBase = docBase;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = StringUtil.trim(onSelect);
	}
}
