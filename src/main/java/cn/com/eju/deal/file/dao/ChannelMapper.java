package cn.com.eju.deal.file.dao;

import cn.com.eju.deal.file.model.Channel;

public interface ChannelMapper {
    int deleteByPrimaryKey(Integer channelId);

    int insert(Channel record);

    int insertSelective(Channel record);

    Channel selectByPrimaryKey(Integer channelId);

    int updateByPrimaryKeySelective(Channel record);

    int updateByPrimaryKey(Channel record);
}