@rem mupload上传组件打包脚本

@echo all      包含普通和tablelayout两种布局管理器
@echo simple   包含普通布局管理器
@echo 其他     包含tablelayout布局管理器
@echo 

@set /P mtype="请输入要生成的客户端类型:"

@if [%mtype%] == [all] goto all
@if [%mtype%] == [simple] goto simple
@goto tablelayout



:simple
@rem 不使用tablelayout布局管理器的客户端
@cd ..\bin

@jar cf mjc-mupload.jar cn\mjc\mupload\MUploadSetting.class
@jar uf mjc-mupload.jar cn\mjc\mupload\FileTypes.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$1$1.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$1.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$2.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$FileUploader.class
@jar uf mjc-mupload.jar cn\mjc\mupload\SelectedFile.class
@jar uf mjc-mupload.jar cn\mjc\mupload\StringUtil.class
@jar uf mjc-mupload.jar cn\mjc\mupload\simple
@jar uf mjc-mupload.jar cn\mjc\mupload\upload

@goto end




:tablelayout
@rem 使用tablelayout布局管理器的客户端

@copy ..\lib\TableLayout-bin-jdk1.5-2007-04-21.jar ..\bin /y
@cd ..\bin
@rename TableLayout-bin-jdk1.5-2007-04-21.jar mjc-mupload.jar

@jar uf mjc-mupload.jar cn\mjc\mupload\MUploadSetting.class
@jar uf mjc-mupload.jar cn\mjc\mupload\FileTypes.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$1$1.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$1.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$2.class
@jar uf mjc-mupload.jar cn\mjc\mupload\BaseMUploadClient$FileUploader.class
@jar uf mjc-mupload.jar cn\mjc\mupload\SelectedFile.class
@jar uf mjc-mupload.jar cn\mjc\mupload\StringUtil.class
@jar uf mjc-mupload.jar cn\mjc\mupload\table
@jar uf mjc-mupload.jar cn\mjc\mupload\upload

@goto end




:all
@rem 同时包含两种的客户端
@copy ..\lib\TableLayout-bin-jdk1.5-2007-04-21.jar ..\bin /y
@cd ..\bin
@rename TableLayout-bin-jdk1.5-2007-04-21.jar mjc-mupload.jar
@jar uf mjc-mupload.jar cn\mjc\mupload

@goto end




:end
@echo 文件打包成功！
pause