package cn.mjc.mupload.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import sun.net.www.protocol.http.HttpURLConnection;
import cn.mjc.mupload.StringUtil;

/**
 * 使用HTTP的表单方式上传时的上传应用类.
 * <P>
 * 
 * 文件上传成功后，系统应该返回最终上传后的文件名称和文件路径等信息，格式如下： true=文件的上传后的路径
 * <P>
 * 
 * @author 马必强
 * 
 */
public class HttpUpload extends AbstractUpload {
	private final static int blockSize = 4096;

	public HttpUpload(BaseFileUploadStatus uploadStatus) {
		super(uploadStatus);
	}

	@Override
	public String upload(File file, BaseFileUploadStatus uploadStatus)
			throws Exception {
		HttpURLConnection con = (HttpURLConnection) new URL(this.getUploadURL())
				.openConnection();
		// System.out.println("len=" + file.length() + "   len=" +
		// ((int)file.length()));
		// System.out.println("con:" + con.getClass());
		// 设置连接参数属性
		con.setDoOutput(true);
		con.setRequestProperty("Accept", "*/*");
		con.setRequestProperty("Accept-Language", "zh-cn");
		con
				.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=---------------------------8d71b5d6290e4");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Cache-Control", "no-cache");
		// 设置流式输出的请求头和请求尾
		String header = "-----------------------------8d71b5d6290e4\r\n"
				+ "Content-Disposition: form-data; name=\"mupload_file\"; filename=\"%s\"\r\n"
				+ "Content-Type: application/octet-stream\r\n\r\n";
		byte[] tail = "\r\n-----------------------------8d71b5d6290e4--\r\n"
				.getBytes();
		byte[] headByte = String.format(header, file.getName()).getBytes();
		// 设置请求体的长度(直接输出,避免在本地进行缓存),同时连接服务器
		con.setFixedLengthStreamingMode((int) file.length() + headByte.length
				+ tail.length);
		con.connect();
		// 写文件流到请求体中
		OutputStream dos = con.getOutputStream();
		// System.out.println("os:" + dos.getClass());
		dos.write(headByte); // 写文件头
		FileInputStream fis = new FileInputStream(file);
		int len = 0;
		byte b[] = new byte[blockSize];
		while (len != -1) {
			len = fis.read(b);
			if (len > 0) {
				dos.write(b, 0, len);
				dos.flush();
				uploadStatus.upload(len);
			}
		}
		fis.close();
		dos.write(tail);
		dos.close();
		uploadStatus.dataTransportComplete(); // 数据传输完毕

		// 获取响应流.如果响应输出不是指定的格式则认为是上传失败
		InputStream is = con.getInputStream();
		byte[] response = new byte[is.available()];
		is.read(response);
		return new String(response).trim();
	}

	/**
	 * 获取接收上传的服务器地址.
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	private String getUploadURL() {
		String httpURL = StringUtil.trim(this.setting.getHttpURL());
		if (httpURL.startsWith("http://"))
			return httpURL;
		URL doc = this.setting.getDocBase();
		String base = "http://" + doc.getHost()
				+ (doc.getPort() == -1 ? "" : (":" + doc.getPort()));
		if (httpURL.startsWith("/"))
			return base + httpURL;
		String path = StringUtil.trim(doc.getPath()).intern();
		if (path == "" || path == "/")
			return base + "/" + httpURL;
		int index = path.lastIndexOf("/");
		if (index <= 0)
			return base + "/" + httpURL;
		return base + path.substring(0, index) + "/" + httpURL;
	}
}
