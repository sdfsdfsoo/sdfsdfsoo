package patentsearch.job;

import java.util.Iterator;
import java.util.List;


import patentsearch.utils.base.*;

import patentsearch.bean.base.Patent;
import patentsearch.bean.cndescriptionitem.CNDescriptionItem;
import patentsearch.bean.search.requestParameter.DoSearchParameter;
import patentsearch.bean.search.requestParameter.GetGeneralDataParameter;
import patentsearch.service.base.PatentService;
import patentsearch.service.patent.SearchService;

public class PatentMonitorSearch {
	//private EntityManagerFactory emf; 
	//private EntityManager em = emf.createEntityManager();
	
	//private static SearchService searchService = new SearchServiceImpl();
	
	//private static PatentService patentService = new PatentServiceImpl(); //SpringContextHolder.getBean(PatentServiceImpl.class);
	
	/*
	 * 测试用,正式改成配置文件
	 */
	private String searchFormula = "F XX (扬中/AB+扬中/CL+扬中/TI+扬中/IN+扬中/PA+扬中/AT+扬中/DZ)";

	/*
	 * 度应的user是zjcyxx
	 */
	private int userId = 43;

	//private String searchFormulaOld = "F XX (扬中/AB_扬中/CL_扬中/TI_扬中/IN_扬中/PA_扬中/AT_扬中/DZ)";



	private Long total;
	
	private Integer rows = 50;
	private Integer page = 1;
	
	private List<CNDescriptionItem> cNDescriptionItemList;
	private SearchService searchService;
	
	private PatentService patentService;
	
	public void tableSearch() {
		searchService = SpringContextHolder.getBean("searchService");
		patentService = SpringContextHolder.getBean("patentService");


		String strDateUID = (int) (Math.random() * 1000) + "";
		try {
			patentService.delAllPatent();
			
			DoSearchParameter doSearch = new DoSearchParameter(userId + ""
					+ strDateUID, "Cn", "999", searchFormula);
			total = searchService.handleDoSearch(doSearch);
			
			if(total > 0){
				long totalpages = total/rows;
				for(int i = 1; i <= totalpages + 1; i++){
					page = i;
					GetGeneralDataParameter getGeneralDataParameter = new GetGeneralDataParameter(userId +""+strDateUID, "Cn", "999", page, rows);
					cNDescriptionItemList = searchService.handleGetGeneralData(getGeneralDataParameter);
					Iterator<CNDescriptionItem> ir = cNDescriptionItemList.iterator();
					while(ir.hasNext()){
						CNDescriptionItem o = new CNDescriptionItem();
						Patent p = new Patent();
						o = (CNDescriptionItem)ir.next();
						p.setAppno(o.getAppno());
						p.setPubnr(o.getPubnr());
						p.setAppnr(o.getAppnr());
						p.setApd(o.getApd());
						p.setPud(o.getPud());
						p.setGrpd(o.getGrpd());
						p.setAppd(o.getAppd());
						p.setNc(o.getNc());
						p.setAgency(o.getAgency());
						p.setAddress(o.getAddress());
						p.setAgent(o.getAgent());
						p.setTitle(o.getTitle());
						p.setZip(o.getZip());
						p.setIpcMain(o.getIpcMain());
						p.setIpcChild(o.getIpcMinor());
						p.setAppl(o.getAppl());
						p.setInventor(o.getInventor());		
						//p.setId(System.currentTimeMillis());
						
						patentService.save(p);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
