package cn.com.eju.deal.student.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.base.service.BaseService;
import cn.com.eju.deal.core.support.QueryConst;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.dto.student.StudentDto;
import cn.com.eju.deal.student.dao.StudentMapper;
import cn.com.eju.deal.student.model.Student;

/**   
* service层
* @author li_xiaodong
* @date 2016年2月2日 下午7:57:09
*/
@Service("studentService")
public class StudentService extends BaseService
{
    
    @Resource
    private StudentMapper studentMapper;
    
    /**
    * 日志
    */
    private final LogHelper logger = LogHelper.getLogger(this.getClass());
    
    /** 
    * 查询
    * @param id
    * @return
    */
    
    public Student getById(int id)
        throws Exception
    {
        Student student = studentMapper.getById(id);
        return student;
    }
    
    /** 
     * 查询-list
     * @param queryParam
     * @return
     */
    
    public ResultData<List<StudentDto>> queryList(Map<?, ?> param)
        throws Exception
    {
        
        //构建返回
        ResultData<List<StudentDto>> resultData = new ResultData<List<StudentDto>>();
        
        //查询
        final List<Student> moList = studentMapper.queryList(param);
        
        //转换
        List<StudentDto> dtoList = convertData(moList);
        
        resultData.setTotalCount((String)param.get(QueryConst.TOTAL_COUNT));
        
        resultData.setReturnData(dtoList);
        
        return resultData;
    }
    
    /** 
     * 创建
     * @param param
     * @return
     */
    
    public int create(Student student)
        throws Exception
    {
        int count = studentMapper.create(student);
        return count;
    }
    
    /** 
     * 更新
     * @param param
     * @return
     */
    
    public int update(Student student)
        throws Exception
    {
        int count = studentMapper.update(student);
        return count;
    }
    
    /** 
    * 删除
    * @param id 
    * @param updateId 更新人
    * @param updateTime 更新时间
    * @return
    */
    
    public int delete(int id, int updateId)
        throws Exception
    {
        int count = studentMapper.deleteById(id, updateId);
        return count;
    }
    
    /** 
     * 对象转换MO--DTO
     * @param stuList
     * @return List<StudentDto>
     */
    private List<StudentDto> convertData(List<Student> moList)
        throws Exception
    {
        List<StudentDto> dtoList = new ArrayList<StudentDto>();
        
        if (null != moList && !moList.isEmpty())
        {
            StudentDto dto = null;
            for (Student mo : moList)
            {
                dto = new StudentDto();
                BeanUtils.copyProperties(mo, dto);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
    
}
