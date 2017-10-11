package cn.com.eju.deal.file.handle;

/**   
 * weiphoto 帮助类
* weiphoto文件上传--获取文件路径、文件下载 
* @author (li_xiaodong)
* @date 2016年5月28日 下午4:46:55
*/
public class WeiphotoHelper
{
    
    
    /** 
    * 获取图片路径(原图片文件)
    * @param fileCode "720-960-42e972556d09a1fdc280f0eb8e391264"
    * @param size 100
    * @return http://183.136.160.249:2080/pic/{pid}/{size}
    */
    public static String getPicPath(String fileUrl, String fileCode)
        throws Exception
    {
        String filePath = String.format(fileUrl, fileCode, "2000");
        
        return filePath;
    }
    
    /** 
    * 获取图片路径(特定图片文件)
    * @param fileCode "720-960-42e972556d09a1fdc280f0eb8e391264"
    * @param size 100
    * @return http://183.136.160.249:2080/pic/{pid}/{size}
    */
    public static String getPicPath(String fileUrl, String fileCode, String size)
        throws Exception
    {
        String filePath = String.format(fileUrl, fileCode, size);
        
        return filePath;
    }
    
    /** 
    * 获取文件路径
    * @param fileCode "720-960-42e972556d09a1fdc280f0eb8e391264"
    * @param size 100
    * @return http://183.136.160.249:2080/file/{pid}
    */
    public static String getFilePath(String fileUrl, String fileCode)
        throws Exception
    {
        String filePath = String.format(fileUrl, fileCode);
        
        return filePath;
    }
    
}
