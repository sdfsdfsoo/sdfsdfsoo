package patentsearch.utils.base;
 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.cndescriptionitem.EnDescriptionItem;
import patentsearch.bean.user.SearchFormula;
import patentsearch.bean.util.xml.DateUtil;
import patentsearch.service.legalstatus.LegalStatusService;

public class ExcelTool2007 {

	/**
	 * 导出专利信息
	 */
	public static void exportPatentsToExcel(List<CNDescriptionItem> resultList,String fileSavePath,String fileSaveName ,String items)   {
		
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("new   sheet");
			HSSFCellStyle style = wb.createCellStyle(); // 样式对象
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short) 11); // 字体高度
			style.setFont(font);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			HSSFRow row = sheet.createRow((short) 0);
			row.setHeight((short) 500);// 目的是想把行高设置成25px
			HSSFCell cell = row.createCell( 0);// 1
			cell.setCellValue("序号");
			cell.setCellStyle(style);  
			cell = row.createCell(1);
			cell.setCellValue("申请号");
			cell.setCellStyle(style);  
			cell = row.createCell(2); 
			cell.setCellValue("名称");
			cell.setCellStyle(style);  
			cell = row.createCell( 3); 
			cell.setCellValue("申请日");
			cell.setCellStyle(style);  
			cell = row.createCell(4); 
			cell.setCellValue("分类号");
			cell.setCellStyle(style);  
			cell = row.createCell(5); 
			cell.setCellValue("公开（公告）日");
			cell.setCellStyle(style); 
			cell = row.createCell(6); 
			cell.setCellValue("专利代理机构");
			cell.setCellStyle(style);  
			cell = row.createCell(7); 
			cell.setCellValue("主权项");
			cell.setCellStyle(style);  
			cell = row.createCell(8); 
			cell.setCellValue("国省代码");
			cell.setCellStyle(style);  
			cell = row.createCell(9); 
			cell.setCellValue("申请（专利权）人");
			cell.setCellStyle(style);  
			cell = row.createCell(10); 
			cell.setCellValue("发明（设计）人");
			cell.setCellStyle(style);  
			cell = row.createCell(11); 
			cell.setCellValue("地址");
			cell.setCellStyle(style);  
			cell = row.createCell(12); 
			cell.setCellValue("法律状态");
			cell.setCellStyle(style);  
			cell = row.createCell(13); 
			cell.setCellValue("摘要");
			cell.setCellStyle(style); 
			cell = row.createCell(14); 
			cell.setCellValue("主权利要求书");
			cell.setCellStyle(style); 
			int temp=0;
			for(CNDescriptionItem cnDescriptionItem :resultList){
				temp++;
				 row = sheet.createRow(temp);
					row.setHeight((short) 500);// 目的是想把行高设置成25px
					
					cell = row.createCell(0); 
					cell.setCellValue(temp);      //申请号
					cell.setCellStyle(style);  
					
				cell = row.createCell(1); 
				cell.setCellValue(cnDescriptionItem.getAppno());      //申请号
				cell.setCellStyle(style);  
				cell = row.createCell(2); 
				cell.setCellValue(cnDescriptionItem.getTitle());     //名称
				cell.setCellStyle(style);  
				cell = row.createCell(3); 
				if(items.contains("appdate")){                   //items包含的是所选要到处的著录项内容    申请日
					cell.setCellValue(DateUtil.dateToTextString(cnDescriptionItem.getApd()));
				}else{
					cell.setCellValue("");
				}
				cell.setCellStyle(style);  
				cell = row.createCell(4); 
				if(items.contains("ipc")){        
					cell.setCellValue(cnDescriptionItem.getIpcMain());    //分类号
				}else{
					cell.setCellValue("");    //分类号
				}
				cell.setCellStyle(style);  
				cell = row.createCell(5); 
				if(items.contains("pubdate")){  
					if(cnDescriptionItem.getGrd()==null||"".equals(cnDescriptionItem.getGrd())){
						cell.setCellValue(DateUtil.dateToTextString(cnDescriptionItem.getPud()));   //公开（公告）日
					}else{
						cell.setCellValue(DateUtil.dateToTextString(cnDescriptionItem.getAppd()));   //公开（公告）日
					}
					
				}else{
					cell.setCellValue("");   //公开（公告）日
				}
				cell.setCellStyle(style);  
				cell = row.createCell(6); 
				if(items.contains("agent")){        
					cell.setCellValue(cnDescriptionItem.getAgency());   //专利代理机构
				}else{
					cell.setCellValue("");   //专利代理机构
				}
				cell.setCellStyle(style);  
				cell = row.createCell(7); 
				if(items.contains("claim")){        
					cell.setCellValue(cnDescriptionItem.getClaim());    //主权项
				}else{
					cell.setCellValue("");    //主权项
				}
				cell.setCellStyle(style);  
				cell = row.createCell(8); 
				if(items.contains("countryNC")){        
					cell.setCellValue(cnDescriptionItem.getNc());    //国省代码
				}else{
					cell.setCellValue("");    //国省代码
				}
				cell.setCellStyle(style);  
				cell = row.createCell(9); 
				if(items.contains("appl")){        
					cell.setCellValue(cnDescriptionItem.getAppl());    //申请（专利权）人
				}else{
					cell.setCellValue("");    //申请（专利权）人
				}
				cell.setCellStyle(style); 
				cell = row.createCell(10); 
				if(items.contains("inventor")){        
					cell.setCellValue(cnDescriptionItem.getInventor());    //发明（设计）人
				}
				cell.setCellStyle(style);  
				cell = row.createCell(11); 
				if(items.contains("address")){        
					cell.setCellValue(cnDescriptionItem.getAddress());   //地址
				}else{
					cell.setCellValue("");   //地址
				}
				cell.setCellStyle(style); 
				cell = row.createCell(12); 
				if(items.contains("flzt")){        
					cell.setCellValue(cnDescriptionItem.getCnLegalStatusStr());   //法律状态
//					cell.setCellValue("-------");   //法律状态
				}else{
					cell.setCellValue("");   
				}
				cell.setCellStyle(style);  
				cell = row.createCell(13); 
				if(items.contains("abstr")){        
					cell.setCellValue(cnDescriptionItem.getAbstr());   //法律状态
//					cell.setCellValue("-------");   //法律状态
				}else{
					cell.setCellValue("");   
				}
				cell.setCellStyle(style);  
				cell = row.createCell(14); 
				if(items.contains("claim")){        
					cell.setCellValue(cnDescriptionItem.getClaim());   //法律状态
//					cell.setCellValue("-------");   //法律状态
				}else{
					cell.setCellValue("");   
				}
				cell.setCellStyle(style);  
			}
			
			// 保存路径
			String savePath = ServletActionContext.getServletContext()
					.getRealPath(fileSavePath+fileSaveName);

			FileOutputStream fileOut = new FileOutputStream(savePath);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 导出专利信息(国外)
	 */
	public static void exportPatentsToExcelEN(List<EnDescriptionItem> resultList,String fileSavePath,String fileSaveName)   {
		try {

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("new   sheet");
			HSSFCellStyle style = wb.createCellStyle(); // 样式对象
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short) 11); // 字体高度
			style.setFont(font);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			HSSFRow row = sheet.createRow((short) 0);
			row.setHeight((short) 500);// 目的是想把行高设置成25px
			HSSFCell cell = row.createCell( 0);// 1
			cell.setCellValue("申请号");
			cell.setCellStyle(style);  
			cell = row.createCell(1); 
			cell.setCellValue("专利名称");
			cell.setCellStyle(style);  
			cell = row.createCell( 2); 
			cell.setCellValue("申请日");
			cell.setCellStyle(style);  
			cell = row.createCell(3); 
			cell.setCellValue("摘要");
			cell.setCellStyle(style);  
			cell = row.createCell(4); 
			cell.setCellValue("公开/公告号");
			cell.setCellStyle(style); 
			cell = row.createCell(5); 
			cell.setCellValue("公开/公告日");
			cell.setCellStyle(style);  
			cell = row.createCell(6); 
			cell.setCellValue("申请/专利权人");
			cell.setCellStyle(style);  
			cell = row.createCell(7); 
			cell.setCellValue("发明/设计人");
			cell.setCellStyle(style);  
			cell = row.createCell(8); 
			cell.setCellValue("国际主分类号");
			cell.setCellStyle(style);  
			cell = row.createCell(9); 
			cell.setCellValue("国际分类号");
			cell.setCellStyle(style);  
			cell = row.createCell(10); 
			cell.setCellValue("优先权项");
			cell.setCellStyle(style);  
			cell = row.createCell(11); 
			cell.setCellValue("国际申请号");
			cell.setCellStyle(style); 
			cell = row.createCell(12); 
			cell.setCellValue("国际申请日");
			cell.setCellStyle(style);  
			cell = row.createCell(13); 
			cell.setCellValue("国际公布号");
			cell.setCellStyle(style);  
			cell = row.createCell(14); 
			cell.setCellValue("国际公布日");
			cell.setCellStyle(style);  
			cell = row.createCell(15); 
			cell.setCellValue("文献种类代码");
			cell.setCellStyle(style);  
			cell = row.createCell(16); 
			cell.setCellValue("欧洲主分类号");
			cell.setCellStyle(style);
			cell = row.createCell(17); 
			cell.setCellValue("欧洲分类号");
			cell.setCellStyle(style);  
			cell = row.createCell(18); 
			cell.setCellValue("美国主分类号");
			cell.setCellStyle(style);  
			cell = row.createCell(19); 
			cell.setCellValue("美国分类号");
			cell.setCellStyle(style);  
			int temp=0;
			for(EnDescriptionItem enDescriptionItem :resultList){
				temp++;
				 row = sheet.createRow(temp);
					row.setHeight((short) 500);// 目的是想把行高设置成25px
				cell = row.createCell(0); 
				cell.setCellValue(enDescriptionItem.getAppno());  //申请号
				cell.setCellStyle(style);  
				cell = row.createCell(1); 
				cell.setCellValue(enDescriptionItem.getTitle());  //专利名称
				cell.setCellStyle(style);  
				cell = row.createCell(2); 
				cell.setCellValue(DateUtil.formatStrToStr(enDescriptionItem.getApdText()));   //申请日
				cell.setCellStyle(style);  
				cell = row.createCell(3); 
				cell.setCellValue(enDescriptionItem.getAbs());    //摘要
				cell.setCellStyle(style);  
				cell = row.createCell(4); 
				cell.setCellValue(enDescriptionItem.getPubnr());     //公开/公告号
				cell.setCellStyle(style);  
				cell = row.createCell(5); 
				cell.setCellValue(DateUtil.formatStrToStr(enDescriptionItem.getPudText()));    //公开/公告日
				cell.setCellStyle(style);  
				cell = row.createCell(6); 
				cell.setCellValue(enDescriptionItem.getAppl());                   //申请/专利权人
				cell.setCellStyle(style);  
				cell = row.createCell(7); 
				cell.setCellValue(enDescriptionItem.getInventor());                 //发明/设计人
				cell.setCellStyle(style);  
				cell = row.createCell(8); 
				cell.setCellValue("");           //国际主分类号
				cell.setCellStyle(style); 
				cell = row.createCell(9); 
				cell.setCellValue(enDescriptionItem.getInterIpc());     //国际分类号
				cell.setCellStyle(style);  
				cell = row.createCell(10); 
				cell.setCellValue(enDescriptionItem.getPris());      //优先权项
				cell.setCellStyle(style); 
				cell = row.createCell(11); 
				cell.setCellValue("");      //国际申请号
				cell.setCellStyle(style); 
				cell = row.createCell(12); 
				cell.setCellValue("");      //国际申请日
				cell.setCellStyle(style); 
				cell = row.createCell(13); 
				cell.setCellValue("");      //国际公布号
				cell.setCellStyle(style); 
				cell = row.createCell(14); 
				cell.setCellValue("");      //国际公布日
				cell.setCellStyle(style); 
				cell = row.createCell(15); 
				cell.setCellValue("");      //文献种类代码
				cell.setCellStyle(style); 
				cell = row.createCell(16); 
				cell.setCellValue("");      //欧洲主分类号
				cell.setCellStyle(style); 
				cell = row.createCell(17); 
				cell.setCellValue(enDescriptionItem.getEuroIpc());      //欧洲分类号
				cell.setCellStyle(style); 
				cell = row.createCell(18); 
				cell.setCellValue("");      //美国主分类号
				cell.setCellStyle(style); 
				cell = row.createCell(19); 
				cell.setCellValue("");      //美国分类号
			}
			
			// 保存路径
			String savePath = ServletActionContext.getServletContext()
					.getRealPath(fileSavePath+fileSaveName);

			FileOutputStream fileOut = new FileOutputStream(savePath);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * 导出算式
	 */
	public static void exportFormulasToExcel(List<SearchFormula> searchFormulaList,String fileSavePath,String fileSaveName)    {
		try {
			//System.out.println("searchFormulaList:"+ searchFormulaList);
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("new   sheet");
			HSSFCellStyle style = wb.createCellStyle(); // 样式对象

			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short) 11); // 字体高度

			style.setFont(font);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			HSSFRow row = sheet.createRow((short) 0);
			row.setHeight((short) 500);// 目的是想把行高设置成25px

			HSSFCell cell = row.createCell( 0); 
			cell.setCellValue("检索时间");
			cell.setCellStyle(style);  

			cell = row.createCell(1); 
			cell.setCellValue("检索编号");
			cell.setCellStyle(style);  

			cell = row.createCell( 2); 
			cell.setCellValue("检索式");
			cell.setCellStyle(style);  

			cell = row.createCell(3); 
			cell.setCellValue("命中数");
			cell.setCellStyle(style);  
			
			int temp=0;
			for(SearchFormula searchFormula :searchFormulaList){
				temp++;
				 row = sheet.createRow(temp);
					row.setHeight((short) 500);// 目的是想把行高设置成25px
				cell = row.createCell(0); 
				cell.setCellValue(DateUtil.dateToCnTextString(searchFormula.getAlterTime()));
				cell.setCellStyle(style);  
				
				cell = row.createCell(1); 
				
				cell.setCellValue(searchFormula.getItemID());
				cell.setCellStyle(style);  
				
				cell = row.createCell(2); 
				if(null==searchFormula.getFormula()||searchFormula.getFormula().equals("")){
					cell.setCellValue("");
				}
				else{
					cell.setCellValue(searchFormula.getFormula());
				}
				cell.setCellStyle(style);  
				
				cell = row.createCell(3); 
				
				if(null==searchFormula.getHits()||searchFormula.getHits().equals("")){
					cell.setCellValue("");
				}
				else{
					cell.setCellValue(searchFormula.getHits());
				}
				
				
				cell.setCellStyle(style);  
 
			}
			
			// 保存路径
			String savePath = ServletActionContext.getServletContext()
					.getRealPath(
							fileSavePath+fileSaveName);

			FileOutputStream fileOut = new FileOutputStream(savePath);
			wb.write(fileOut);
			fileOut.close();
		 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}