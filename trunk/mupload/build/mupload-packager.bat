@rem mupload�ϴ��������ű�

@echo all      ������ͨ��tablelayout���ֲ��ֹ�����
@echo simple   ������ͨ���ֹ�����
@echo ����     ����tablelayout���ֹ�����
@echo 

@set /P mtype="������Ҫ���ɵĿͻ�������:"

@if [%mtype%] == [all] goto all
@if [%mtype%] == [simple] goto simple
@goto tablelayout



:simple
@rem ��ʹ��tablelayout���ֹ������Ŀͻ���
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
@rem ʹ��tablelayout���ֹ������Ŀͻ���

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
@rem ͬʱ�������ֵĿͻ���
@copy ..\lib\TableLayout-bin-jdk1.5-2007-04-21.jar ..\bin /y
@cd ..\bin
@rename TableLayout-bin-jdk1.5-2007-04-21.jar mjc-mupload.jar
@jar uf mjc-mupload.jar cn\mjc\mupload

@goto end




:end
@echo �ļ�����ɹ���
pause