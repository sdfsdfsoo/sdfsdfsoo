package patentsearch.web.action.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

/**
 * 存储action的共有属性
 *
 */
public class BaseAction {
	/*附件,多工作表的Excel好像不支持*/
	protected File file;
	/*获取上传文件的文件名，属性名写法固定*/
	protected String fileFileName;
	/*获取上传文件的类型，写法仍然固定*/
	protected String fileContentType;
	/*允许上传的文件类型*/
	private static Properties arrowTypes = new Properties();
	//静态初始化块
	static{
		try {
			arrowTypes.load(BaseAction.class.getClassLoader().getResourceAsStream("arrowuploadfiletype.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	
	/**
	 * 获取上传文件的扩展名
	 * @return
	 */
	public String getExt(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();//获取文件的扩展名
	}
	/**
	 * 判断文件类型是否为图片
	 * @param file
	 * @return
	 */
	public boolean volidateImageFileType(File file,String fileContentType){
		if(file != null && file.length()>0){
			List<String> arrowType = Arrays.asList("image/bmp","image/png","image/gif","image/jpeg","image/pjpeg");
			if(arrowType.contains(fileContentType.toLowerCase())){
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断文件类型是否为允许上传的类型
	 * @param file
	 * @return
	 */
	public boolean volidateFileType(File file,String fileContentType,String fileFileName){
		if(file != null && file.length()>0){
			List<String> arrowType = new ArrayList<String>();
			String ext = getExt(fileFileName);//获取文件的扩展名如：jpg
			for(Object key : arrowTypes.keySet()){
				String value = (String) arrowTypes.get(key);
				String[] types = value.split(",");//一种文件可能有多种格式，如image/jpeg",image/pjpeg
				for(String type : types){
					arrowType.add(type.trim());
				}
			}
			if(arrowType.contains(fileContentType) && arrowTypes.keySet().contains(ext)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 生成文件保存路径
	 * @return
	 */
	protected String generateFileSavePath(){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
		//数据库所要存储的相对图片路径
		String uploadpath = "/upload/" + dateformat.format(new Date());
		return uploadpath;
	}
	
	protected String generateFileSaveName(String fileFileName){
		String ext = this.getExt(fileFileName);
		String fileSaveName = UUID.randomUUID().toString() + "." +ext;///生成文件名
		return fileSaveName;
	}
	/**
	 * 保存文件到指定目录
	 * @param pathdir 文件保存目录
	 * @return
	 */
	protected void saveFile(File saveFile, String pathdir, String fileSaveName){
		if(saveFile!=null && saveFile.length()>0){
			//获取图片的真实存储路径
			String realPath = ServletActionContext.getServletContext().getRealPath(pathdir);
			File filepathdir = new File(realPath);
			if(!filepathdir.exists()){
				filepathdir.mkdirs();
			}
			try {
				FileUtils.copyFile(saveFile, new File(filepathdir,fileSaveName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return fileSaveName;
		}
		//return null;
	}
}
