## 说明 ##
> mupload是采用java applet技术构建的基于标准http协议的大文件上传组件，mupload的特点就是：

  1. 支持大文件上传，最大支持1.5G的文件
  1. 服务器无关，由于采用标准的HTTP协议进行文件传输，因此任何只要支持http协议的语言都可以作为服务端；
  1. 前台兼容性广，目前经过测试，可以很好的兼容IE、FireFox、Chrome等主流浏览器；
  1. 开发友好，开发者可以自定义组件提供的很多参数
  1. 用户体验好，组件上传会显示进度、上传速度、剩余时间等基本信息



## 快速应用 ##

### 源码打包部署 ###

  * 下载mjc-mupload-1.1.zip文件并解压
  * 通过控制台进入mjc-mupload目录，输入如下命令(要保证本机安装了maven2)：
```
  # 以下命令只需要在机器上执行一次，来安装mupload的依赖到maven的本地仓库
  cd src/main/resources/lib
  ./install_lib_to_maven2.bat
  # 打包mupload
  cd ../../../..
  mvn clean install
```
> > 执行完毕后，会在target/signed目录下得到经过签名后的jar包，这是可以发布的jar包。
  * 复制target/singed目录下的jar包和src/main/resources/js/mjc-mupload-1.1.js到你的应用的发布目录即可。


### 设置签名 ###


> 组件默认已经带了一个签名store文件，在src/main/resources/sign目录下，默认使用的密码是mupload，如果你想提供自己的签名信息，请按照以下步骤进行：
  1. 通过控制台进入src/main/resources/sign目录；
  1. 输入 **keytool -genkey -keystore mjc-mupload.store -alias mupload** 生成store文件，注意密码请设置为mupload，如果不一样请修改POM文件中的签名密码为你的密码
  1. 生成后即完成了签名信息的设置，以后只需要通过MAVEN打包就可以使用你的签名信息了。


### 示例程序应用 ###

> 组件也提供了一个示例程序供大家使用和学习，按照如下步骤进行部署与运行：
  1. 下载mjc-mupload-example-1.1.zip文件并解压
  1. 实例程序中已经包含了最新的mupload包和js文件，在src/main/webapp/mupload目录下(mjc-mupload-1.1.jar和mjc-mupload-1.1.js)；如果你想使用自己打包的mupload文件，请覆盖该目录下的jar包即可；
  1. 然后在该压缩包的跟目录，输入如下命令打包：mvn clean install
  1. 运行完毕后，系统在target目录下生成了一个mjc-mupload-example-1.1.war文件，复制这个文件到TOMCAT的webapps下即可完成部署
  1. 启动TOMCAT，成功后，在浏览器中输入：http://localhost:8080/mjc-mupload-example-1.1即可访问


### 如何应用到自己的应用中 ###

> 应用mupload到自己的系统中和示例中一样，可以参考一下示例程序，其过程如下：
  1. 将前面打包生成的mjc-mupload-1.1.jar和mupload-1.1.js文件拷贝到你的系统某一个目录(这个目录应该是可以通过HTTP访问到的)
  1. 在该目录下写一个类似示例程序中的mupload.html文件，这里主要是包含了mupload-1.1.js文件，用来创建UI界面并执行文件上传，参数请参考项目首页的介绍！
  1. 在需要执行文件上传的页面中调用第2步中的HTML文件即可打开上传界面进行文件上传。
