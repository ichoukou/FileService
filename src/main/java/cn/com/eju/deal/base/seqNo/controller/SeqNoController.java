package cn.com.eju.deal.base.seqNo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.eju.deal.base.controller.BaseController;
import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.base.seqNo.service.SeqNoService;
import cn.com.eju.deal.core.support.ResultData;

/**   
* 
* @author (li_xiaodong)
* @date 2016年3月20日 下午12:29:31
*/
@RestController
@RequestMapping(value = "seqnos")
public class SeqNoController extends BaseController
{
    
    /**
    * 日志
    */
    private final LogHelper logger = LogHelper.getLogger(this.getClass());
    
    @Resource(name = "seqNoService")
    private SeqNoService seqNoService;
    
    /** 
     * 查询-全部对象
     * @param id
     * @return
     */
    @RequestMapping(value = "/{typeCode}", method = RequestMethod.GET)
    public String getSeqNo(@PathVariable String typeCode)
    {
        
        //查询
        ResultData<?> resultData = new ResultData<>();
        try
        {
            resultData = seqNoService.getSeqNoByTypeCode(typeCode);
        }
        catch (Exception e)
        {
            resultData.setFail();
            logger.error("base", "SeqNoController", "getSeqNo", "", null, "", "", e);
        }
        
        return resultData.toString();
    }
    
}
