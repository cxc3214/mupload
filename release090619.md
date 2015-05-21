## 更新说明 ##
> mupload在2009.06.19发布1.1版，增加了UI参数控制，可以选择不同的UI布局，同时项目架构采用MAVEN2，可以更好的管理项目的开发与发布。

  * 在JS中增加了UI控制参数，可以直接指定不同的UI布局，目前仅有table和simple两种；
  * 项目的源码结构采用MAVEN2的结构模式，可以更好的进行开发与发布管理；
  * 同时通过MAVEN2可以自动进行打包签名了，无需用户手动进行签名操作；
  * 重写了测试代码，可以测试新的特性(2种UI选择)，同时也修改了之前发布的contextpath的问题；


## 用户指南 ##
### 新的项目结构 ###
> 按照标准的MAVEN2项目结构进行组织和管理源码，有几点说明如下：
  * 源码编译依赖一个tablelayout布局类和netscape的JS与applet的交互类，放在resources\lib目录下，POM文件中设置了依赖，但由于这是非标准且不好找的JAR包，因此，需要运行目录下的批处理文件安装这2个jar包到本地的MAVEN仓库！这个步骤应该在你打包mupload之前进行，避免编译mupload时出现类找不到的问题。

  * resources\js目录下存放的是js的源码和处理后的js文件，发布时需要拷贝mjc-mupload-1.1.js到你的发布目录

  * resources\sign目录是签名信息设置目录，这里有一个mjc-mupload.store是签名时要用到的文件，如果你要使用自己的store文件，那么请保证密码是mupload或者修改POM文件中的密码为你的密码，然后再运行MAVEN打包时就不会出现签名失败了。同时生成的jar包在target目录下的是为签名的，在target/signed目录下的是经过签名的，我们发布时需要发布经过签名后的jar包。


### 例子程序使用 ###
> 例子程序也重新组织过了，发布的例子也是采用MAVEN2项目的结构来组织的，同时里面已经包含了最新的mupload的jar包(经过签名后)。直接运行：mvn clean install即生成WAR包，然后部署到TOMCAT等服务器后就可以运行了。

> 如果你需要重新打包mupload并在例子中进行测试，那么在打包mupload成功后，需要到复制target/signed目录下的jar包到例子程序的webapp/mupload目录下，js可以不需要重新发布。然后再打包例子程序就可以了使用新的mupload了。