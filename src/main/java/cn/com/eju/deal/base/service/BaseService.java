package cn.com.eju.deal.base.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.eju.core.util.EncryptUtil;

import cn.com.eju.deal.base.helper.LogHelper;
import cn.com.eju.deal.base.model.PageInfo;
import cn.com.eju.deal.base.support.FYResultData;
import cn.com.eju.deal.base.support.SystemParam;
import cn.com.eju.deal.core.support.Constant;
import cn.com.eju.deal.core.support.QueryConst;
import cn.com.eju.deal.core.support.ResultData;
import cn.com.eju.deal.core.support.ReturnCode;
import cn.com.eju.deal.core.util.JsonUtil;
import cn.com.eju.deal.core.util.StringUtil;

/**
 * 调用REST服务类的基类
 * 
 * @author (li_xiaodong)
 * @date 2016年2月16日 下午3:10:06
 * @param <T>
 */
public abstract class BaseService<T> {

	/**
	 * 日志
	 */
	private final LogHelper logger = LogHelper.getLogger(this.getClass());

	/**
	 * Spring提供的用于访问Rest服务的客户端
	 */
	private RestTemplate restTemplate = new RestTemplate();

	/**
	 * GET, 不带参数
	 * 
	 * @param url
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> get(String url) throws Exception {
		Object obj = null;
		return get(url, obj);
	}

	/**
	 * GET, 带参数，参数类型Object... urlVariables
	 * 
	 * @param url
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> get(String url, Object... urlVariables) throws Exception {
		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("BaseService request url=" + url + "; request params: urlVariables=" + urlVariables);

		// HTTP GET
		ResultData<?> backResult = new ResultData<T>();

		try {
			// HTTP GET
			backResult = restTemplate.getForObject(url, ResultData.class, urlVariables);

		} catch (Exception e) {
			// logger.error(moduleName, className, methodName, dto.toString(),
			// operateId, ipAddress, description, e);
			throw e;
		}

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("BaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(backResult)
				+ "; cost time:(" + (endTime - startTime) + ") ms");

		return backResult;
	}

	/**
	 * GET, 默认不分页、带参数，参数类型（Map<String, ?>）
	 * 
	 * @param url
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> get(String url, Map<String, Object> reqMap) throws Exception {
		return get(url, reqMap, null);

	}

	/**
	 * GET, 分页、带参数，参数类型（Map<String, ?>）
	 * 
	 * @param url
	 * @param reqMap
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> get(String url, Map<String, Object> reqMap, PageInfo pageInfo) throws Exception {
		if (null != pageInfo) {
			reqMap.put(QueryConst.PAGE_IDX, pageInfo.getCurPage().toString());
			reqMap.put(QueryConst.PAGE_SIZE, pageInfo.getPageLimit().toString());
		}

		// 数据权限标示
		Boolean needAuth = (Boolean) reqMap.get(Constant.DATA_AUTH_KEY);
		if (null != needAuth && needAuth) {
			// 获取当前用户及其下属用户Id集合, 用于数据权限过滤
			// List<Integer> idsList = getUserIdList();

			// reqMap.put("userIdList", idsList);
		}

		// 日志记录begin
		long startTime = System.currentTimeMillis();

		// 记录日志
		logger.info("BaseService request url=" + url + "; request params:" + JsonUtil.parseToJson(reqMap));

		// HTTP GET
		ResultData<?> reback = restTemplate.getForObject(url, ResultData.class, JsonUtil.parseToJson(reqMap));

		// 日志记录end
		long endTime = System.currentTimeMillis();

		// 记录日志
		logger.info("BaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(reback)
				+ "; cost time:(" + (endTime - startTime) + ") ms");

		if (null != pageInfo && null != reback) {

			// 记录总数
			String total = (String) reback.getTotalCount();

			if (StringUtil.isNotEmpty(total)) {

				pageInfo.setDataCount(Integer.valueOf(total));
			}
		}

		return reback;

	}

	/**
	 * post 请求 -- 鉴权
	 * 
	 * @param url
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> post(String url, T dto) throws Exception {

		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("BaseService request url=" + url + "; request params:" + dto.toString());

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<T> formEntity = new HttpEntity<T>(dto, headers);

		ResultData<?> backResult = new ResultData<T>();

		try {
			// HTTP POST
			backResult = restTemplate.postForObject(url, formEntity, ResultData.class);
		} catch (Exception e) {
			// logger.error(moduleName, className, methodName, dto.toString(),
			// operateId, ipAddress, description, e);
			throw e;
		}

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("BaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(backResult)
				+ "; cost time:(" + (endTime - startTime) + ") ms");

		return backResult;
	}

	/**
	 * 修改
	 * 
	 * @param url
	 * @param dto
	 * @throws Exception
	 */
	public void put(String url, T dto) throws Exception {
		// 默认需要鉴权
		put(url, dto, true);
	}

	/**
	 * 修改--鉴权
	 * 
	 * @param url
	 * @param dto
	 * @param isUserAuth
	 *            是否需要鉴权
	 * @throws Exception
	 */
	public void put(String url, T dto, boolean isUserAuth) throws Exception {
		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("BaseService request url=" + url + "; request params:" + dto.toString());

		HttpHeaders headers = new HttpHeaders();

		// 需要鉴权
		if (isUserAuth) {
			Map<String, Object> authMap = new HashMap<String, Object>();
			// authMap.put("userCode", userInfo.getUserCode());
			// authMap.put("userName", userInfo.getUserName());

			try {
				headers.add("authInfo", URLEncoder.encode(JsonUtil.parseToJson(authMap), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

		HttpEntity<T> formEntity = new HttpEntity<T>(dto, headers);

		try {
			// HTTP PUT
			restTemplate.put(url, formEntity, dto);
		} catch (Exception e) {
			// logger.error(moduleName, className, methodName, dto.toString(),
			// operateId, ipAddress, description, e);
			throw e;
		}

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("BaseService response url=" + url + "; response data:" + "HTTP PUT 请求" + "; cost time:("
				+ (endTime - startTime) + ") ms");

	}

	/**
	 * 删除
	 * 
	 * @param url
	 * @param id
	 * @param updateId
	 * @throws Exception
	 */
	public void delete(String url, Object id) throws Exception {
		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("BaseService request url=" + url + "; request params: id=" + id);

		try {
			// HTTP DELETE
			restTemplate.delete(url, id);
		} catch (Exception e) {
			// logger.error(moduleName, className, methodName, dto.toString(),
			// operateId, ipAddress, description, e);
			throw e;
		}

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("BaseService response url=" + url + "; response data:" + "HTTP DELETE 请求" + "; cost time:("
				+ (endTime - startTime) + ") ms");

	}

	/**
	 * 删除
	 * 
	 * @param url
	 * @param id
	 * @param updateId
	 * @throws Exception
	 */
	public void delete(String url, Object id, int updateId) throws Exception {
		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("BaseService request url=" + url + "; request params: id=" + id + ",updateId=" + updateId);

		try {
			// HTTP DELETE
			restTemplate.delete(url, id, updateId);
		} catch (Exception e) {
			// logger.error(moduleName, className, methodName, dto.toString(),
			// operateId, ipAddress, description, e);
			throw e;
		}

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("BaseService response url=" + url + "; response data:" + "HTTP DELETE 请求" + "; cost time:("
				+ (endTime - startTime) + ") ms");

	}
	
	   /**
     * post 请求 -- 鉴权
     * 
     * @param url
     * @param dto
     * @return
     * @throws Exception
     */
    public ResultData<?> postForRMS(String url, Map<String, Object> reqMap) throws Exception {

        // 日志记录begin
        long startTime = System.currentTimeMillis();
        logger.info("BaseService request url=" + url + "; request params:" + reqMap.toString());

        String dtoJson = JsonUtil.parseToJson(reqMap);

        String sign = JsonUtil.parseToJson(reqMap).toLowerCase() + "POST" + SystemParam.getWebConfigValue("RMS_Api_Secret");

        sign = EncryptUtil.encrypt(sign, EncryptUtil.EncryptType.MD5);
        sign = EncryptUtil.encrypt(sign, EncryptUtil.EncryptType.MD5);

        HttpHeaders headers = new HttpHeaders();
        //
        headers.add("appId", SystemParam.getWebConfigValue("RMS_Api_AppId"));
        headers.add("sign", sign);
        
        T dto = (T)JsonUtil.parseToObject(dtoJson, Object.class);

        HttpEntity<T> formEntity = new HttpEntity<T>(dto, headers);

        // 构建返回
        ResultData<T> backResult = new ResultData<T>();

        // 请求RMS响应
        FYResultData<?> fyResultData = new FYResultData<T>();

        try {
            // HTTP POST
            fyResultData = restTemplate.postForObject(url, formEntity, FYResultData.class);
        } catch (Exception e) {
            throw e;
        }

        String returnCode = fyResultData.getDataCode();

        // 成功转换返回200
        if (ReturnCode.FY_SUCCESS.equals(returnCode)) {
            returnCode = ReturnCode.SUCCESS;
        }

        // 返回转换
        backResult.setReturnCode(returnCode);
        backResult.setReturnMsg(fyResultData.getMessage());
        backResult.setTotalCount(String.valueOf(fyResultData.getTotalCount()));
        backResult.setReturnData((T) fyResultData.getResult());

        // 日志记录end
        long endTime = System.currentTimeMillis();
        logger.info("BaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(backResult)
                + "; cost time:(" + (endTime - startTime) + ") ms");

        return backResult;
    }

	/**
	 * post 请求 -- 鉴权
	 * 
	 * @param url
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> postForRMS(String url, T dto) throws Exception {

		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("BaseService request url=" + url + "; request params:" + dto.toString());

		String dtoJson = JsonUtil.parseToJson(dto).toLowerCase();

		String sign = dtoJson + "POST" + SystemParam.getWebConfigValue("RMS_Api_Secret");

		sign = EncryptUtil.encrypt(sign, EncryptUtil.EncryptType.MD5);
		sign = EncryptUtil.encrypt(sign, EncryptUtil.EncryptType.MD5);

		HttpHeaders headers = new HttpHeaders();
		//
		headers.add("appId", SystemParam.getWebConfigValue("RMS_Api_AppId"));
		headers.add("sign", sign);

		HttpEntity<T> formEntity = new HttpEntity<T>(dto, headers);

		// 构建返回
		ResultData<T> backResult = new ResultData<T>();

		// 请求RMS响应
		FYResultData<?> fyResultData = new FYResultData<T>();

		try {
			// HTTP POST
			fyResultData = restTemplate.postForObject(url, formEntity, FYResultData.class);
		} catch (Exception e) {
			throw e;
		}

		String returnCode = fyResultData.getDataCode();

		// 成功转换返回200
		if (ReturnCode.FY_SUCCESS.equals(returnCode)) {
			returnCode = ReturnCode.SUCCESS;
		}

		// 返回转换
		backResult.setReturnCode(returnCode);
		backResult.setReturnMsg(fyResultData.getMessage());
		backResult.setTotalCount(String.valueOf(fyResultData.getTotalCount()));
		backResult.setReturnData((T) fyResultData.getResult());

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("BaseService response url=" + url + "; response data:" + JsonUtil.parseToJson(backResult)
				+ "; cost time:(" + (endTime - startTime) + ") ms");

		return backResult;
	}

	/**
	 * post 请求 -- For RMS
	 * 
	 * @param url
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public ResultData<?> postForRMS(String url, T dto, PageInfo pageInfo) throws Exception {
		// 构建返回
		ResultData<T> backResult = new ResultData<T>();

		// 日志记录begin
		long startTime = System.currentTimeMillis();
		logger.info("request url=" + url + "; request params:" + dto.toString());

		String dtoJson = JsonUtil.parseToJson(dto).toLowerCase();

		String sign = dtoJson + "POST" + SystemParam.getWebConfigValue("RMS_Api_Secret");

		sign = EncryptUtil.encrypt(sign, EncryptUtil.EncryptType.MD5);
		sign = EncryptUtil.encrypt(sign, EncryptUtil.EncryptType.MD5);

		HttpHeaders headers = new HttpHeaders();
		//
		headers.add("appId", SystemParam.getWebConfigValue("RMS_Api_AppId"));
		headers.add("sign", sign);

		HttpEntity<T> formEntity = new HttpEntity<T>(dto, headers);

		// 请求RMS响应
		FYResultData<?> fyResultData = new FYResultData<T>();

		try {
			// HTTP POST
			fyResultData = restTemplate.postForObject(url, formEntity, FYResultData.class);
		} catch (Exception e) {
			// logger.error(moduleName, className, methodName, dto.toString(),
			// operateId, ipAddress, description, e);
			throw e;
		}

		// 日志记录end
		long endTime = System.currentTimeMillis();
		logger.info("response url=" + url + "; response data:" + JsonUtil.parseToJson(backResult) + "; cost time:("
				+ (endTime - startTime) + ") ms");

		String returnCode = fyResultData.getDataCode();

		// 成功转换返回200
		if (ReturnCode.FY_SUCCESS.equals(returnCode)) {
			returnCode = ReturnCode.SUCCESS;
		}

		// 返回转换
		backResult.setReturnCode(returnCode);
		backResult.setReturnMsg(fyResultData.getMessage());
		backResult.setTotalCount(String.valueOf(fyResultData.getTotalCount()));
		backResult.setReturnData((T) fyResultData.getResult());

		// 带分页
		if (null != pageInfo && null != backResult) {

			// 记录总数
			String total = (String) backResult.getTotalCount();

			if (StringUtil.isNotEmpty(total)) {

				pageInfo.setDataCount(Integer.valueOf(total));
			}
		}

		return backResult;
	}

	/**
	 * (根据相对路径生成绝对路径)
	 * 
	 * @param relativeUrl
	 *            相对路径
	 * @param systemCode
	 *            目标系统Code
	 * @return
	 * @throws Exception
	 */
	public String absoluteUrl(String systemCode, String relativeUrl) throws Exception {
		// 接口绝对地址
		String absoluteUrl = SystemParam.getWebConfigValue(systemCode + "RestServer") + relativeUrl;

		return absoluteUrl;
	}
}
