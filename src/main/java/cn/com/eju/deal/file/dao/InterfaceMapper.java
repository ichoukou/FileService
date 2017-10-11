package cn.com.eju.deal.file.dao;

import java.util.Map;

import cn.com.eju.deal.file.model.Interface;

public interface InterfaceMapper
{
    
    /** 
    * 获取渠道信息
    * @param reqMap
    * @return
    * @throws Exception
    */
    Interface getChannelInfo(Map<?, ?> reqMap)
        throws Exception;
}