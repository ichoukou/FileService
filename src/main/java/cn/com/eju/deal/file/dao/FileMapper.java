package cn.com.eju.deal.file.dao;

import java.util.List;

import cn.com.eju.deal.core.dao.IDao;
import cn.com.eju.deal.file.model.Files;

public interface FileMapper extends IDao<Files>
{
    /** 
    * 根据fileNo获取Files
    * @param relateId
    * @return
    * @throws Exception
    */
    Files getByFileNo(String fileNo)
        throws Exception;
    
    /** 
    * 根据fileNos获取List<Files>
    * @param relateId
    * @return
    * @throws Exception
    */
    List<Files> getFileList(String[] fileNoArr)
        throws Exception;
    
    /** 
    * 根据fileNo删除
    * @param fileNo
    * @param operateId
    * @return
    * @throws Exception
    */
    int deleteByFileNo(String fileNo)
        throws Exception;
}