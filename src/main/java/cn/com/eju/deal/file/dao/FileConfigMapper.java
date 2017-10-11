package cn.com.eju.deal.file.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.file.model.FileConfig;

/**   
* 文件系统配置
* @author li_xiaodong
* @date 2017年3月10日 下午4:58:53
*/
@Repository(value="fileConfigMapper")
public interface FileConfigMapper extends IDao<FileConfig>
{
    /** 
    * 获取配置
    * @param channelCode
    * @return
    */
    List<FileConfig> getCfgByChannelCode(@Param(value = "channelCode") String channelCode);
}