package patentsearch.job;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class PatentMonitorSearchJob implements Job{
	private PatentMonitorSearch patentMonitorSearch = new PatentMonitorSearch();
	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//扬中
		System.out.println("start = " + System.currentTimeMillis());
		patentMonitorSearch.tableSearch();
		System.out.println("end = " + System.currentTimeMillis());
	}
}
