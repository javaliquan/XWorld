package com.founderdpt.comm.xworld.base;

import java.util.List;
import java.util.Map;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQSequence;


import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.orm.XMLConfigClientTemplate;
import com.founderdpt.comm.xworld.orm.exception.XMLDataAccessException;
import com.founderdpt.comm.xworld.orm.page.Page;

public abstract class BaseDao {
	protected XMLConfigClientTemplate template = new XMLConfigClientTemplate();

	protected abstract String getNameSpace();

	/**
	 * 根据查询参数查询某个对象
	 * @param xpath 配置文件中查询语句的xpath路径
	 * @param clazz 查询对象
	 * @param paramObj 查询参数
	 * @return 
	 * @throws XMLDataAccessException
	 */
	public <T> T queryForObject(String xpath, Class<T> clazz, Object paramObj)
			throws XMLDataAccessException {
		return template.queryForObject(getNameSpace(), xpath, clazz, paramObj);
	}
    
	/**
	 * 根据查询参数查询对象列表
	 * @param xpath 配置文件中查询语句的xpath路径
	 * @param clazz 查询对象
	 * @param paramObj 查询参数
	 * @return
	 * @throws XMLDataAccessException
	 */
	public <T> List<T> queryForList(String xpath, Class<T> clazz,
			Object paramObj) throws XMLDataAccessException {
		return template.queryForList(getNameSpace(), xpath, clazz, paramObj);
	}
	
	/**
	 * 根据查询参数查询对象总数
	 * @param clazz 查询对象
	 * @param where Xquery 中的 where子句
	 * @param paramObj 查询参数
	 * @return
	 */
	public <T> long count(Class<T> clazz,String where,Object paramObj) {
		return template.count(clazz, where, paramObj);
	}
	
	/**
	 * 查询结果以Map形式返回，查询语句中的return 满足<map><key></key><value></value></map>
	 * @param xpath 配置文件中查询语句的xpath路径
	 * @param paramObj 查询参数
	 * @return
	 */
	public Map queryForMap(String xpath, Object paramObj) {
		return template.queryForMap(getNameSpace(), xpath, paramObj);
	}
	/**
	 * 根据查询参数查询对象列表，分页查询
	 * @param xpath 配置文件中查询语句的xpath路径
	 * @param clazz 查询对象 
	 * @param paramObj 查询参数
	 * @param pageNo 查询起始位置从1开始
	 * @param pageSize 查询个数
	 * @return
	 * @throws XMLDataAccessException
	 */
	public <T> List<T> queryForList(String xpath, Class<T> clazz,
			Object paramObj, int pageNo, int pageSize)
			throws XMLDataAccessException {
		return template.queryForList(getNameSpace(), xpath, clazz, paramObj,
				pageNo, pageSize);
	}
	public <T> Page<T> queryForPageList(Class<T> clazz,String where,Object paramObj ,Page<T> page) {
		return template.queryForPageList(clazz, where, paramObj, page);
	}

	public String queryForSring(String xpath, Object paramObj)
			throws XMLDataAccessException {
		return template.queryForSring(getNameSpace(), xpath, paramObj);
	}

	/**
	 * 往数据库里面添加一条记录
	 * @param paramObj 待保存的javaBean对象
	 * @param v_schema 是否需要schema验证，true表示需要
	 */
	public void save(Object paramObj, boolean v_schema) {
		template.save(paramObj, v_schema);
	}
    /**
     * 往数据库里面添加一条记录,需要schema验证
     * @param paramObj 待保存的javaBean对象
     */
	public void save(Object  paramObj ) {
		template.save(paramObj, true);
	}
	/**
	 * 根据id查询单个对象
	 * @param id 对象id
	 * @param clazz 查询对象 
	 * @return
	 */
	public   <T> T get(String id,Class<T> clazz){
		return template.get(id, clazz);
	}
	
	/**
	 * 根据id删除对象
	 * @param id 对象id
	 * @param clazz 查询对象
	 */
	public   void delete(String id,Class clazz){
		  template.delete(id, clazz);
	}

	/**
	 * 更新对象
	 * @param paramObj 新对象数据
	 */
	public   void update(Object paramObj){
		  template.update(paramObj);
	}
	
	/**
	 * 更新对象
	 * @param paramObj 新对象数据
	 * @param v_schema 是否需要schema验证，true表示需要
	 */
	public   void update(Object paramObj,boolean v_schema){
		  template.update(paramObj,v_schema);
	}
	
	/**
	 * 查询对象列表，分页查询
	 * @param clazz 查询对象
	 * @param pageNo 查询起始位置，从1开始
	 * @param pageSize 查询个数
	 * @return
	 */
	public <T> List<T> queryForList(Class<T> clazz, Integer pageNo,
			Integer pageSize) {
		return template.queryForList(clazz,pageNo,pageSize);
	}
	
	
	public <T> Page<T> queryForList(String xpath,
			Class<T> clazz, Object paramObj,Page page) {
		return template.queryForList(getNameSpace(),xpath,clazz,paramObj,page);
	}
	/**
	 * 根据查询参数查询总数
	 * @param xpath 配置文件中查询语句的xpath路径
	 * @param clazz 查询对象
	 * @param paramObj 查询参数
	 * @return
	 */
	public <T> Long  queryCount(String xpath,
			Class<T> clazz, Object paramObj) {
		return template.queryCount(getNameSpace(),xpath,clazz,paramObj);
	}
	
	
	/**
	 * 查询所有的对象
	 * @param clazz 查询对象
	 * @return
	 */
	public <T> List<T> queryForList(Class<T> clazz) {
		return template.queryForList(clazz);
	}
	
	/**
	 * 执行xquery语句
	 * @param query xquery语句
	 * @return
	 * @throws XQException
	 */
	public String executeQuery(String query) throws XQException {
		return getSession().executeQuery(query);
	}

	/**
	 * 得到IXQSession
	 * @return
	 */
	public IXQSession getSession() {
		return template.getSession();
	}

	/**
	 * 执行xquery语句,返回 XQSequence
	 * @param query xquery语句
	 * @return
	 * @throws XQException
	 */
	public XQSequence executeQuerySequence(String query) throws XQException {
		return getSession().executeQuerySequence(query);
	}
}
