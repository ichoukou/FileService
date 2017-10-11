package cn.com.eju.deal.core.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.eju.deal.core.model.JobModel;

/**   
* TODO (这里用一句话描述这个文件的作用)
* @author 
* @date 2016年3月10日 上午10:32:32
*/
public class QuartzManager
{
    
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    
    private static Scheduler sched;
    static
    {
        SchedulerFactory sf = new StdSchedulerFactory();
        try
        {
            sched = sf.getScheduler();
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }
    
    /** 
     * 启动一个自定义的job 
     *  
     * @param schedulingJob 
     *            自定义的job 
     * @param paramsMap 
     *            传递给job执行的数据 
     * @param isStateFull 
     *            是否是一个同步定时任务，true：同步，false：异步 
     * @return 成功则返回true，否则返回false 
     */
    public static boolean enableCronSchedule(JobModel schedulingJob, JobDataMap paramsMap, boolean isStateFull)
    {
        if (schedulingJob == null)
        {
            return false;
        }
        try
        {
            
            logger.info(schedulingJob.getStateFulljobExecuteClass().toString());
            logger.info(schedulingJob.getJobId());
            logger.info(schedulingJob.getJobGroup());
            logger.info(paramsMap.toString());
            JobDetail job =
                newJob(schedulingJob.getStateFulljobExecuteClass()).withIdentity(schedulingJob.getJobId(),
                    schedulingJob.getJobGroup())
                    .usingJobData(paramsMap)
                    .build();
            CronTrigger triggers =
                newTrigger().withIdentity(schedulingJob.getJobId() + "Trigger", schedulingJob.getJobGroup())
                    .withSchedule(cronSchedule(schedulingJob.getCronExpression()))
                    .build();
            sched.scheduleJob(job, triggers);
            sched.start();
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /** 
     * 禁用一个job 
     *  
     * @param jobId 
     *            需要被禁用的job的ID 
     * @param jobGroupId 
     *            需要被警用的jobGroupId 
     * @return 成功则返回true，否则返回false 
     */
    public static boolean disableSchedule(String jobId, String jobGroupId)
    {
        if (jobId.equals("") || jobGroupId.equals(""))
        {
            return false;
        }
        try
        {
            Trigger trigger = QuartzManager.getJobTrigger(jobId, jobGroupId);
            if (trigger != null)
            {
                //TriggerKey triggerKey=new TriggerKey(jobId+"Trigger", jobGroupId);
                //sched.unscheduleJob(triggerKey);  
                logger.info(sched.getJobDetail(new JobKey(jobId, jobGroupId)).toString());
                sched.deleteJob(new JobKey(jobId, jobGroupId));
                logger.info(sched.getJobDetail(new JobKey(jobId, jobGroupId)).toString());
            }
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /** 
     * 得到job对应的Trigger 
     *  
     * @param jobId 
     *            job的ID 
     * @param jobGroupId 
     *            job的组ID 
     * @return job的Trigger,如果Trigger不存在则返回null 
     */
    public static Trigger getJobTrigger(String jobId, String jobGroupId)
    {
        if (jobId.equals("") || jobGroupId.equals("") || null == jobId || jobGroupId == null)
        {
            return null;
        }
        try
        {
            
            TriggerKey triggerKey = new TriggerKey(jobId + "Trigger", jobGroupId);
            return sched.getTrigger(triggerKey);
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
}
