# Introduction #

关于示例在某些情况下无法上传的问题。


# Details #

> 关于示例在某些情况下无法上传的问题，这个主要是因为你没有将示例的WEB目录设置为resin或tomcat等服务器的\*ROOT根目录**，否则运行时获取的URL地址因为没有包含context path而出错。**

> 可以参考看下代码中关于获取上传URL的代码片段就可以知道原因了：
```
private String getUploadURL() {
    String httpURL = StringUtil.trim(this.setting.getHttpURL()); // 这个就是我们设置的URL
    if (httpURL.startsWith("http://")) return httpURL;
    URL doc = this.setting.getDocBase(); // 这个是获取当前包含控件的页面URL
    String base = "http://" + doc.getHost() + (doc.getPort() == -1 ? "" : (":" + doc.getPort()));
    if (httpURL.startsWith("/")) return base + httpURL;
    String path = StringUtil.trim(doc.getPath()).intern();
    if (path == "" || path == "/") return base + "/" + httpURL;
    int index = path.lastIndexOf("/");
    if (index <= 0) return base + "/" + httpURL;
    return base + path.substring(0, index) + "/" + httpURL;
}
```