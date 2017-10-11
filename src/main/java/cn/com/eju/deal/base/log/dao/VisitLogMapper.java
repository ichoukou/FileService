package cn.com.eju.deal.base.log.dao;

import org.springframework.stereotype.Repository;

import cn.com.eju.deal.base.log.model.VisitLog;
import cn.com.eju.deal.core.dao.IDao;

/**   
* <p>访问日志类  DAO</p>
* @author li_xiaodong
* @date 2017年2月10日 上午10:31:13
*/
@Repository(value="visitLogMapper")
public interface  VisitLogMapper extends IDao<VisitLog>
{
   
}
