package com.founderdpt.comm.xworld.orm;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.xquery.XQSequence;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.founderdpt.comm.xworld.db.DptXMLDBSessionFactory;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.orm.exception.TransactionException;
import com.founderdpt.comm.xworld.orm.exception.XMLDataAccessException;
import com.founderdpt.comm.xworld.orm.expression.ExpressionBindUtil;
import com.founderdpt.comm.xworld.orm.mapping.MappingManager;
import com.founderdpt.comm.xworld.orm.mapping.MappingXqueryManager;
import com.founderdpt.comm.xworld.orm.mapping.SchemaManager;
import com.founderdpt.comm.xworld.orm.mapping.generator.GeneratorFactory;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator.Param;
import com.founderdpt.comm.xworld.orm.mapping.model.result.JAXBMap;
import com.founderdpt.comm.xworld.orm.page.Page;
import com.founderdpt.comm.xworld.util.config.XMLConfigUtil;
import com.founderdpt.comm.xworld.util.config.builder.DynamicContext;
import com.founderdpt.comm.xworld.util.xml.JAXBUtil;

public class XMLConfigClientTemplate {
	private static Logger log = Logger.getLogger(XMLConfigClientTemplate.class);

	@SuppressWarnings("unchecked")
	public <T> T queryForObject(String nameSpace, String xpath, Class<T> clazz,
			Object paramObj) {
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		T obj = null;
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			String query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}

			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query);
			}
			XQSequence sequence = session.executeQuerySequence(query, bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			if (sequence.next()) {
				obj = (T) unmarshaller.unmarshal(new StringReader(sequence
						.getItemAsString(null)));
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("查询结果" + obj);
		}
		return obj;
	}

	public Map queryForMap(String nameSpace, String xpath, Object paramObj) {
		Map map = new HashMap();
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		String query = "";
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}
			// 得到需要bind的数据
			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);

			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query);
			}
			XQSequence sequence = session.executeQuerySequence(result_query,
					bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(JAXBMap.class);
			while (sequence.next()) {
				String obj_value = sequence.getItemAsString(null);
				JAXBMap obj = (JAXBMap) unmarshaller
						.unmarshal(new StringReader(obj_value));
				map.put(obj.getJaxb_key(), obj.getJaxb_value());
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + query, e);
		}
		return map;
	}

	public <T> List<T> queryForList(String nameSpace, String xpath,
			Class<T> clazz, Object paramObj) {
		List<T> list = new ArrayList<T>();
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		String query = "";
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}
			// 得到需要bind的数据
			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);

			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query);
			}
			XQSequence sequence = session.executeQuerySequence(result_query,
					bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			while (sequence.next()) {
				String obj_value = sequence.getItemAsString(null);
				T obj = (T) unmarshaller.unmarshal(new StringReader(obj_value));
				list.add(obj);
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + query, e);
		}
		return list;
	}

	public <T> List<T> queryForList(Class<T> clazz) {
		return queryForList(clazz, null, null);
	}

	public <T> Page<T> queryForPageList(Class<T> clazz, String where,
			Object paramObj, Page<T> page) {
		List<T> list = new ArrayList<T>();
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		StringBuilder result_query_b = new StringBuilder();
		try {
			StringBuilder query = MappingXqueryManager.getSelectQuery(clazz,
					where);
			DynamicContext context = new DynamicContext();
			context.setParameter(paramObj);
			List bind_list = ExpressionBindUtil.bindParamMap(query.toString(),
					context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);
			if (page == null) {
				result_query_b.append("let $xworld_xquery:=(");
				result_query_b.append(result_query);
				result_query_b.append(") return (count($xworld_xquery),");
				result_query_b.append("$xworld_xquery)");
				page = new Page<T>();
			} else {
				int pageNo = page.getPageNo();
				int pageSize = page.getPageSize();
				if (pageNo > 0 && pageSize >= 0) {
					result_query_b.append("let $xworld_xquery:=(");
					result_query_b.append(result_query);
					result_query_b.append(") return (count($xworld_xquery),");
					result_query_b.append("subsequence($xworld_xquery,");
					result_query_b.append(pageNo);
					result_query_b.append(",");
					result_query_b.append(pageSize);
					result_query_b.append("))");
				} else {
					throw new XMLDataAccessException("查询参数错误  pageNo:" + pageNo
							+ " pageSize:" + pageSize);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query_b);
			}
			XQSequence sequence = session.executeQuerySequence(
					result_query_b.toString(), bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			int i = 0;
			while (sequence.next()) {
				String obj_value = sequence.getItemAsString(null);
				if (i == 0) {
					i++;
					page.setRowCount(Long.valueOf(obj_value));
				} else {
					T obj = (T) unmarshaller.unmarshal(new StringReader(
							obj_value));
					list.add(obj);
				}
			}
			page.setResult(list);
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + result_query_b, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("查询结果:" + list);
		}
		return page;
	}

	public <T> long count(Class<T> clazz, String where, Object paramObj) {
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		StringBuilder result_query_b = new StringBuilder();
		long result = 0L;
		try {
			StringBuilder query = MappingXqueryManager.getSelectQuery(clazz,
					where);
			DynamicContext context = new DynamicContext();
			context.setParameter(paramObj);
			List bind_list = ExpressionBindUtil.bindParamMap(query.toString(),
					context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);
			result_query_b.append("let $xworld_xquery:=(");
			result_query_b.append(result_query);
			result_query_b.append(") return (count($xworld_xquery))");
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query_b);
			}
			XQSequence sequence = session.executeQuerySequence(
					result_query_b.toString(), bind_map);
			result = Long.valueOf(sequence.getSequenceAsString(null));
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + result_query_b, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("查询结果:" + result);
		}
		return result;
	}

	public <T> List<T> queryForList(Class<T> clazz, Integer pageNo,
			Integer pageSize) {
		List<T> list = new ArrayList<T>();
		StringBuilder query = new StringBuilder();
		try {
			IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
			checkConn(session);
			if (pageNo != null && pageSize != null) {
				query.append("subsequence((");
				query.append(MappingXqueryManager.getSelectQuery(clazz));
				query.append("),");
				query.append(pageNo);
				query.append(",");
				query.append(pageSize);
				query.append(")");
				if (log.isDebugEnabled()) {
					log.debug("查询语句" + query.toString());
				}
			} else {
				query = MappingXqueryManager.getSelectQuery(clazz);
			}
			XQSequence sequence = session
					.executeQuerySequence(query.toString());
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			while (sequence.next()) {
				String obj_value = sequence.getItemAsString(null);
				T obj = (T) unmarshaller.unmarshal(new StringReader(obj_value));
				list.add(obj);
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + query.toString(), e);
		}
		if (log.isDebugEnabled()) {
			log.debug("查询结果:" + list);
		}
		return list;
	}

	public <T> List<T> queryForList(String nameSpace, String xpath,
			Class<T> clazz, Object paramObj, int pageNo, int pageSize) {
		List<T> list = new ArrayList<T>();
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		String query = "";
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}
			// 得到需要bind的数据
			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);
			StringBuilder result_query_b = new StringBuilder();
			result_query_b.append("subsequence((");
			result_query_b.append(result_query);
			result_query_b.append("),");
			result_query_b.append(pageNo);
			result_query_b.append(",");
			result_query_b.append(pageSize);
			result_query_b.append(")");
			// result_query="subsequence(("+result_query+"),"+pageNo+","+pageSize+")";
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query_b);
			}

			XQSequence sequence = session.executeQuerySequence(
					result_query_b.toString(), bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			while (sequence.next()) {
				String obj_value = sequence.getItemAsString(null);
				T obj = (T) unmarshaller.unmarshal(new StringReader(obj_value));
				list.add(obj);
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + query, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("查询结果:" + list);
		}
		return list;
	}

	public String queryForSring(String nameSpace, String xpath, Object paramObj) {
		String result = null;
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		String query = "";
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}
			// 得到需要bind的数据
			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);

			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query);
			}
			result = session.executeQuerySequence(result_query, bind_map)
					.getSequenceAsString(null);
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败", e);
		}
		return result;
	}

	public <T> T get(String id, Class<T> clazz) {
		try {
			IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
			checkConn(session);
			StringBuilder query = MappingXqueryManager.getSelectIDQuery(id,
					clazz);
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + query.toString());
			}
			XQSequence sequence = session
					.executeQuerySequence(query.toString());
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			if (sequence.next()) {
				return (T) unmarshaller.unmarshal(new StringReader(sequence
						.getItemAsString(null)));
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败", e);
		}
		return null;
	}

	public Object delete(String id, Class clazz) {
		try {
			IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
			checkConn(session);

			StringBuilder query = new StringBuilder();
			StringBuilder select_query = MappingXqueryManager.getSelectIDQuery(
					id, clazz);

			query.append("delete nodes ");
			query.append(select_query);

			if (log.isDebugEnabled()) {
				log.debug("删除语句" + query.toString());
			}
			XQSequence sequence = session
					.executeQuerySequence(query.toString());
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			if (sequence.next()) {
				return unmarshaller.unmarshal(new StringReader(sequence
						.getItemAsString(null)));
			}
		} catch (Exception e) {
			log.debug("删除失败", e);
			throw new XMLDataAccessException("删除失败", e);
		}
		return null;
	}

	public void update(Object paramObj) {
		update(paramObj, true);
	}

	public void update(Object paramObj, boolean v_schema) {
		try {
			IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
			checkConn(session);
			StringBuilder query = new StringBuilder();

			query.append("replace node ");
			String idvalue = MappingXqueryManager.getIDValue(paramObj);
			URL schema = null;
			Class<? extends Object> clazz = paramObj.getClass();
			if (v_schema) {
				schema = SchemaManager.get(clazz);
			}
			query.append(MappingXqueryManager.getSelectIDQuery(idvalue, clazz));
			query.append("[1] with ");// [1] 就是有可能会出现重复记录，那么就会报错，
			query.append(JAXBUtil.Object2Xml(paramObj, schema));

			if (log.isDebugEnabled()) {
				log.debug("修改语句" + query.toString());
			}
			session.executeQuerySequence(query.toString());
		} catch (Exception e) {
			log.debug("修改失败", e);
			throw new XMLDataAccessException("修改失败", e);
		}
	}

	public String save(Object paramObj) {
		return this.save(paramObj, true);
	}

	public String save(Object paramObj, boolean v_schema) {
		String id = null;
		try {
			IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
			checkConn(session);
			URL schema = null;
			Class<? extends Object> clazz = paramObj.getClass();
			if (v_schema) {
				schema = SchemaManager.get(clazz);
			}
			// 设置ID
			XworldMapping mapping = MappingManager.get(clazz);
			String idColumn = mapping.getId().getName();
			id = BeanUtils.getProperty(paramObj, idColumn);
			if (StringUtils.isBlank(id)) {
				id = MappingXqueryManager.getID(clazz);
				BeanUtils.setProperty(paramObj, idColumn, id);
			}
			// getSequenceValue
			List<Column> columns = mapping.getColumn();
			if (columns != null) {
				for (Column column : columns) {
					XworldMapping.Column.Generator generator = column
							.getGenerator();
					String column_name = column.getName();
					String column_A_value = BeanUtils.getProperty(paramObj, column_name);
					if (StringUtils.isBlank(column_A_value)&&StringUtils.isNotBlank(column_name)) {
							String column_value = GeneratorFactory
									.getGeneratorColumnValue(
											 column, clazz);
							BeanUtils.setProperty(paramObj, column_name, column_value);
					}
				}
			}
			StringBuilder insert_path = MappingXqueryManager.getDocXPath(clazz);

			// String xpath = "insert[@id='"+id+"']";
			StringBuilder query = new StringBuilder(
					"declare boundary-space preserve; insert nodes ");
			query.append(JAXBUtil.Object2Xml(paramObj, schema));
			query.append(" into ").append(insert_path);
			if (log.isDebugEnabled()) {
				log.debug("添加语句" + query);
			}
			// bind_map.put("x_value", value);
			session.executeQuery(query.toString());

		} catch (Exception e) {
			log.debug("添加失败", e);
			throw new XMLDataAccessException("添加失败", e);
		}
		return id;
	}

	public IXQSession getSession() {
		return DptXMLDBSessionFactory.getXQSession();
	}

	public <T> Long queryCount(String nameSpace, String xpath, Class<T> clazz,
			Object paramObj) {
		List<T> list = new ArrayList<T>();
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		String query = "";
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}
			// 得到需要bind的数据
			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);

			StringBuilder result_query_b = new StringBuilder();
			result_query_b.append("count(");
			result_query_b.append(result_query);
			result_query_b.append(")");
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query_b);
			}
			XQSequence sequence = session.executeQuerySequence(
					result_query_b.toString(), bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			String count = sequence.getSequenceAsString(null);

			if (log.isDebugEnabled()) {
				log.debug("查询结果:" + count);
			}
			try {
				return Long.valueOf(count);
			} catch (NumberFormatException e) {
				return 0L;
			}
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + query, e);
		}

	}

	public <T> Page<T> queryForList(String nameSpace, String xpath,
			Class<T> clazz, Object paramObj, Page<T> page) {
		List<T> list = new ArrayList<T>();
		IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
		checkConn(session);
		String query = "";
		try {
			DynamicContext context = XMLConfigUtil.getConfigByXpath(nameSpace,
					XMLConfigUtil.getNamespaceXPath(xpath), paramObj);
			List<String> xql_list = context.getXql_list();
			if (xql_list.isEmpty()) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ "  CODE:"
							+ XMLConfigUtil.getNamespaceXPath(xpath));
				}
				throw new XMLDataAccessException("没有对应的mapper");
			}
			query = xql_list.get(0);
			if (StringUtils.isBlank(query)) {
				if (log.isDebugEnabled()) {
					log.debug("nameSpace:" + nameSpace + "   xpath:" + xpath
							+ " 查询语句为空");
				}
				return null;
			}
			// 得到需要bind的数据
			List bind_list = ExpressionBindUtil.bindParamMap(query, context);
			String result_query = (String) bind_list.get(0);
			Map bind_map = (Map) bind_list.get(1);

			int pageNo = page.getPageNo();
			int pageSize = page.getPageSize();
			StringBuilder result_query_b = new StringBuilder();
			if (pageNo > 0 && pageSize >= 0) {
				result_query_b.append("let $xworld_xquery:=(");
				result_query_b.append(result_query);
				result_query_b.append(") return (count($xworld_xquery),");
				result_query_b.append("subsequence($xworld_xquery,");
				result_query_b.append(pageNo);
				result_query_b.append(",");
				result_query_b.append(pageSize);
				result_query_b.append("))");
			} else {
				throw new XMLDataAccessException("查询参数错误  pageNo:" + pageNo
						+ " pageSize:" + pageSize);
			}
			if (log.isDebugEnabled()) {
				log.debug("查询语句" + result_query_b);
			}
			XQSequence sequence = session.executeQuerySequence(
					result_query_b.toString(), bind_map);
			Unmarshaller unmarshaller = JAXBUtil
					.createUnMarshallerByClazz(clazz);
			int i = 0;
			while (sequence.next()) {
				String obj_value = sequence.getItemAsString(null);
				if (i == 0) {
					i++;
					page.setRowCount(Long.valueOf(obj_value));
				} else {
					T obj = (T) unmarshaller.unmarshal(new StringReader(
							obj_value));
					list.add(obj);
				}
			}
			page.setResult(list);
		} catch (Exception e) {
			log.debug("查询失败", e);
			throw new XMLDataAccessException("查询失败  " + query, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("查询结果:" + list);
		}
		return page;
	}

	private void checkConn(IXQSession session) {
		if (session == null) {
			log.error("没有存在的连接,请检查事务是否开启...");
			throw new TransactionException("事务不存在");
		}
	}

}
