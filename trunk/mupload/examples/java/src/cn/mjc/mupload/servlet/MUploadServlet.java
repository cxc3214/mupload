package cn.mjc.mupload.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mjc.common.utils.fileupload.FileUploader;

/**
 * 文件上传测试服务端.
 * 
 * @author 马必强
 *
 */
public class MUploadServlet extends HttpServlet {
	private static final long serialVersionUID = -1185375939464653684L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws IOException,ServletException {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws IOException,ServletException {
		response.setContentType("text/html;charset=utf-8");
		System.out.println("==============开始文件上传=============");
		FileUploader fu = new FileUploader(request, "upload");
		boolean success = fu.upload();
		if (!success) {
			System.out.println(">>>>>>文件上传失败！");
			write(response, "文件上传失败!");
		} else {
			System.out.println("<<<<<<文件上传成功！");
			write(response, "true=" + fu.getFilePath());
		}
	}
	
	private void write(HttpServletResponse response, String message) throws IOException {
		response.getWriter().write(message);
		response.getWriter().flush();
		response.getWriter().close();
	}
}
