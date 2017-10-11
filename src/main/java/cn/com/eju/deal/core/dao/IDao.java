package cn.com.eju.deal.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**   
* DAO类的基类接口
* @author (li_xiaodong)
* @date 2016年01月30日 上午21:33:40
* @param <T>
*/
public interface IDao<T>
{
    /** 
    * 查询-根据id
    * @param id
    * @return 对象实体
    */
    T getById(int id);
    
    /**
     * 查询-根据查询条件
     * @param param 查询条件集合
     * @return 查询结果
     */
    List<T> queryList(Map<?, ?> param);
    
    /**
     * 查询-根据实体属性查询
     * @param obj 实体属性
     * @return 查询结果
     */
    List<T> getByEntity(T obj);
    
    /**
     * 创建-根据实体对象
     * @param obj 实体对象
     * @return 影响记录条数
     */
    int create(T obj);
    
    /**
     * 创建-批量-根据实体对象list
     * @param objs 实体列表
     * @return 影响记录条数
     */
    int batchCreate(List<T> objs);
    
    /**
     * 更新-根据实体对象 
     * @param obj 实体对象
     * @return 影响记录条数
     */
    int update(T obj);
    
    /**
     * 更新-根据参数
     * @param param 参数
     * @return 影响记录条数
     */
    int updateParam(Map<?, ?> param);
    
    /**
     * 更新-批量
     * @param obj 需要更新的实体内容
     * @param param 参数
     * @return 影响记录条数
     */
    int batchUpdate(@Param(value = "entity") T obj, @Param(value = "queryParam") Map<?, ?> param);
    
    /** 
    * 逻辑删除-根据id,默认将其delFlag置为"Y"
    * @param id
    * @return  影响记录条数
    */
    int deleteById(int id);
    
    /** 
    * 逻辑删除-根据id,记录删除人/删除时间,默认将其delFlag置为"Y"
    * @param id id
    * @param updateId 操作人
    * @param updateTime 操作时间
    * @return
    */
    int deleteById(@Param("id")int id, @Param("updateId")int updateId);
    
    /**
     * 逻辑删除-批量,默认将其delFlag置为"Y"
     * @param 存放主键id的list
     * @return 影响记录条数
     */
    int batchDelete(Map<?, ?> param);
    
    /**
     * 逻辑删除-根据对象,默认将其delFlag置为"Y"
     * @param obj 需要删除的对象
     * @return 影响记录条数
     */
    int deleteObj(T obj);
    
    /**
     * 物理删除--根据id
     * @param id 对象的id
     * @return 影响记录条数
     */
    int phyDelete(int id);
    
    /**
     * 物理删除--对象
     * @param obj 需要删除的对象
     * @return 影响记录条数
     */
    int phyDeleteObj(T obj);
    
    /**
     * 物理删除--批量
     * @param param 批量删除条件
     * @return 影响记录条数
     */
    int batchPhyDelete(Map<?, ?> param);
    
    /**
     * 恢复-根据id，被逻辑删除的对象，默认将其delFlag置为"N"
     * @param id 根据id进行恢复
     */
    void restore(int id);
    
    /**
     * 恢复-根据对象，被逻辑删除的对象，默认将其delFlag置为"N"
     * @param obj 需要恢复的对象
     */
    void restoreObj(T obj);
    
    /**
     * 恢复-批量，被逻辑删除的对象，默认将其delFlag置为"N"
     * @param  param 批量删除条件
     */
    void batchRestore(Map<?, ?> param);
    
}
