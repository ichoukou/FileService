package cn.com.eju.deal.core.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzSingleScheduler
{
    
    private static Scheduler sched = null;
    
    public static Scheduler getInstance()
    {
        if (sched == null)
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
        return sched;
    }
    
}
