## 摘要 ##

> mupload是采用java applet技术构建的，使用标准http协议进行文件上传的组件。打包后的组件经过数字签名后可以嵌入网页，并允许客户端上传文件到服务器，该组件包括一个jar文件和一个js文件，使用js在需要的地方创建该网页组件即可实现文件上传！该组件可以很好的支持IE和FF，同时允许客户端上传最大1.5G的文件，更多的自定义参数允许使用人员定制满足自身的文件上传应用。
mupload是属于个人性质开发的开源组件，采用Apache License开源协议，允许个人和商业用户随意修改和使用！


## 快速资源链接 ##
  * [作者博客网站www.mzone.cc](http://www.mzone.cc)
  * [QuickStart](http://code.google.com/p/mupload/wiki/QuickStart)
  * [mupload1.1版本发布说明](http://code.google.com/p/mupload/wiki/release090619)


## 用户指南 ##

> mupload是基于java applet技术的浏览器插件，用来在网页中使用该插件进行文件上传，以替代传统的FORM上传文件的低效率。整个mupload发布文件包含2个文件：

  * mjc-mupload.jar
> > 客户端插件文件，包含java applet类，用来显示界面和进行文件上传

  * mjc-mupload.js
> > js代码，用来创建显示区域和调用插件以及相关的接口方法，如获取当前文件上传的状态等；


> 通常我们会使用一个弹出窗口来进行文件的上传，这样，我们可以布置成如下目录：
  * mupload(主目录)
    1. mjc-mupload.jar
    1. mjc-mupload.js
    1. upload.html(弹出窗口页面)

> 注意jar和js文件要放在同一个目录，这个后面说到。然后我们在upload.html中增加如下代码：
```
// 首先在页面中引入mjc-mupload.js文件
<script language="javascript" src="./mjc-mupload.js"></script>

// 然后在body中增加如下js代码
<script>
  // 建立MUpload对象
  var upload = new MUpload();

  // 运行控件，设置参数
  upload.run({
    limitTypes : {'图片文件(*.bmp,*.jpg)' : 'bmp,jpg'}, // 只允许上传bmp和jpg格式的图片
    onComplete : 'uploadComplete',
    onFailed : 'uploadFailed',
    httpURL : '/mupload.do'
  });

  // 上传成功后调用的js
  function uploadComplete(localFile, fileSize, serverFilePath) {
    alert('文件上传成功\n本地文件路径：' + localFile + '\n'
        + '文件大小：' + fileSize + '\n'
        + '服务器端路径：' + serverFilePath);
  }

  // 上传失败后调用的js
  function uploadFailed(localFile, fileSize, msg) {
    alert('文件上传失败\n本地文件路径：' + localFile + '\n'
        + '文件大小：' + fileSize + '\n'
        + '失败原因：' + msg);
  }
```

> 保存后，就完成页面的编写任务了，然后就是服务器端的程序开发，一般对于java我们使用fileupload组件来进行文件上传的接收与存储，由于mupload是采用标准的http提交数据的方式来上传文件，因此对于服务器是透明的，无限制的。 **唯一的约束条件就是返回的字符串的格式**，如下：
```
 // 服务器成功接收文件后应该返回如下字符串
 格式：true=文件在服务器的存储路径(这个根据应用程序的需求而定，但必须以true=开头)

 // 文件上传失败时应该返回以非true=开头的字符串，通常为失败的原因
 格式：文件上传失败的原因
```

> 完成客户端和服务器端的开发后整个mupload控件就可以正常使用了!


## js接口参数与方法说明 ##

> 使用mupload文件上传组件时最多交互的地方就是js的交互了，服务器端都是用户自行开发的，与mupload没有任何交互，只是增加了对上传结果返回的格式约束！mupload使用的js是一个对象，必须new之后才可以使用，如下：

```
 // new一个mupload对象
 var instance = new MUpload();

 // 然后必须调用run方法以激活插件，其中options为配置参数，见下面的介绍
 instance.run(options)
```

> 只需要上面两个步骤即可完成mupload的编程工作，options为配置参数，采用JSON的格式，可用的参数如下：
```
 * name
   控件的名称（授权的时候用到)，可以附加公司的相关信息

 * limitTypes
   限制的文件类型，使用对象进行表示，如果不指定或指定为空则默认是全部。
   格式为：{'文件类型描述' : '文件后缀列表','文件类型描述' : '文件后缀列表'}
   对于每一种文件类型，多个文件后缀使用逗号进行分隔！不指定该参数则默认可以选择所有文件

 * limitSize
   限制每个文件的大小，-1不限制，单位为KB

 * uploadType
   文件上传方式 目前只有http-form(为默认的上传类型，可以不指定)

 * appWidth
   applet的宽度，文件上传界面的UI宽度

 * appHeight
   applet的高度，文件上传界面的UI高度

 * jarPath
   mjc-mupload.jar这个jar包的存放路径，相对当前包含该js的页面，默认为mjc-mupload.jar，与js同一目录

 * onComplete
   文件上传成功后的回调方法名称.
   函数的参数依次为：本地的文件路径、文件的大小(单位字节)、上传到服务器后的文件路径

 * onFailed
   文件上传失败的回调方法名称.
   函数的参数依次为：本地的文件路径、文件的大小(单位字节)、失败信息

 * onSelect
   成功选择文件后的回调方法.
   函数的参数依次为：本地的文件路径、文件的大小(单位字节)

 * httpURL
   http方式上传时接收数据的URL地址

 * layout
   指定mupload上传组件的界面布局，可以使用table和simple两种，默认是table布局
```

> 一般来讲我们只需要指定name、limitTypes、limitSize、onComplete、onFailed、onSelect和httpURL参数即可，比如我们例子程序中的：

```
  upload.run({
    limitTypes : {'图片文件(*.bmp,*.jpg)' : 'bmp,jpg'}, // 只允许上传bmp和jpg格式的图片
    onComplete : 'uploadComplete',
    onFailed : 'uploadFailed',
    httpURL : '/mupload.do'
  });
```


> 同时该js还提供了一些方法供外部程序调用以便进行交互，如下：

```
 * MUpload.version
   这是一个静态的全局参数，用来获取当前mupload的版本号

 * status()
   返回当前MUpload实例的状态对象，该方法返回的数据格式为JSON格式：{status:'状态的英文字符串', message:'该状态的中文描述'},所有的状态可用英文字符串如下：
   WAITING_FOR_SELECT_FILE      等待用户选择文件
   WAITING_FOR_UPLOAD           等待用户上传文件（用户已选择文件）
   FILE_UPLOADING               文件上传中
   FILE_SIZE_TOO_LONG           文件大小超出系统最大限制（1.5G）
   FILE_SIZE_OVER_LIMIT         文件大小超出指定大小限制（即options中的limitSize，单位KB）
   FILE_UPLOAD_COMPLETE         文件上传成功
   FILE_UPLOAD_FAILED           文件上传失败（有失败原因）
   

 * run(options)
   运行该实例并显示UI界面，options参数见上面参数部分提到的。

```


## 需要注意的地方 ##

  * 该控件最大支持1.5G的文件上传，超过此限制的文件将不允许上传;

  * 参数中httpURL的设置以及控件中对上传路径的获取规则如下：
    1. 如果httpURL以http://开头则直接使用该地址进行连接，否则继续下面的；
    1. 获取当前页面的路径并得到：base=http:// + 主机 + 端口(如果有的话)
    1. 如果httpURL以/开头，则直接使用base + httpURL作为文件上传地址，否则继续下面的；
    1. 在当前页面的URL的文件区域换成httpURL后作为文件上传路径；

  * 如果你的应用程序有contextPath的话，即使用绝对路径(即httpURL以/开头)，则应该附加contextPath在httpURL前，插件在客户端是无法区分是否有contextPath的。

  * 例子程序必须在应用服务器的ROOT目录下跑才会运行正常，原因即上面的这点。



## 总结 ##

> 今天终于花点时间完善了该项目的用户向导，呵呵，希望该项目可以带给需要的人一点便利，这样我也就欣慰了，有问题和建议欢迎大家拍砖，也很乐于和大家一起探讨技术方面的东西，我的Email:**biqiang.ma@gmail.com**.

