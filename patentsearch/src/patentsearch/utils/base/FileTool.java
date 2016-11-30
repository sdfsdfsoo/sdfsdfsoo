package patentsearch.utils.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import patentsearch.bean.base.PdfEntity;
import patentsearch.service.patent.SearchService;

/**
 * 
 *操作文件的工具类
 */
public class FileTool {
	@Resource
	  SearchService searchService;
	/**
	 * 列出指定文件夹下的所有文件
	 * 
	 * @param dirPath
	 *            文件夹路径
	 * @param filePath
	 *            文件路径，用于组拼出文件的访问路径
	 */
	public static Map<String, String> mapFiles(String dirPath, String filePath) {
		File dir = new File(dirPath);

		if (dir.isDirectory()) {
			// 如果是文件夹
			Map<String, String> fileMap = new LinkedHashMap<String, String>();// 用于存放文件夹下的文件的路径
			File[] files = dir.listFiles();
			for (File file : files) {
				String fileAccessPath = filePath + "/" + file.getName();
				fileMap.put(file.getName(), fileAccessPath);
			}
			return fileMap;
		} else {
			return null;
		}

	}

	public static File generateFile(String fileSavePath, String fileSaveName) {

		String realpath = ServletActionContext.getServletContext().getRealPath(
				fileSavePath);
		File filepathdir = new File(realpath);
		if (!filepathdir.exists()) {
			filepathdir.mkdirs();
		}
		File excelFile = new File(realpath, fileSaveName);
		if (!excelFile.exists()) {
			try {
				excelFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return excelFile;
	}

	/**
	 文件批量并实现打包下载
	 */
	public static void zipFiles(List<PdfEntity> srcfile, File zipfile) {
		byte[] buf = new byte[1024];
		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			// Compress the files
			for (int i = 0; i < srcfile.size(); i++) {
				File file = srcfile.get(i).getPdf();
				FileInputStream in = new FileInputStream(file);
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(srcfile.get(i).getAppno()+".pdf"));
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			}
			 
			// Complete the ZIP file
			 
			out.close();
		} catch (Exception e) {
			 System.out.println("ZipUtil zipFiles exception:" + e);
		}
	}
	 
	public static void main(String[] args) {
		List<File> srcfile = new ArrayList<File>();
		//http://11.0.0.26:8090/epds_nullPdf.pdf
		//http://11.0.0.26:8090/Epds1/CN/2013/203/070/196/000000_20130717_0Y_CN_0.pdf
		/*srcfile.add(new File("D:\\test\\1.png"));
		srcfile.add(new File("D:\\test\\2.png"));
		srcfile.add(new File("D:\\test\\3.png"));
		srcfile.add(new File("D:\\test\\4.png"));
		srcfile.add(new File("http://11.0.0.26:8090/epds_nullPdf.pdf"));
		File zipfile = new File("D:\\test\\edm.zip");
		 zipFiles(srcfile, zipfile);*/
		 String src="http://11.0.0.26:8090/Epds1/CN/2013/203/070/196/000000_20130717_0Y_CN_0.pdf";
		 
		String s1=src.substring(src.indexOf("8090")+5);
		//System.out.println(s1);
		String s2=s1.replace("/", "\\\\");
		//System.out.println("Z:\\\\"+s2);
		
	}
}
