package com.founderdpt.comm.xworld.util.config.builder;

import java.util.Collection;
import java.util.Map;


import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.founderdpt.comm.xworld.orm.exception.ParseConfigException;
import com.founderdpt.comm.xworld.orm.parsing.XNode;

public class ForeachXQLNode implements XQLNode {

	@Override
	public boolean apply(XNode node, DynamicContext context) {
		// TODO Auto-generated method stub
		//List<XNode> foreach_Nodes = node.evalNodes("foreach");
		Map map_backup = context.getBindings();
		//HashMap map = map_backup;
		//XNode foreach_Node = foreach_Nodes.get(0);
		String collection = node.getNode().getAttributes().getNamedItem("collection")
		.getNodeValue();
		//String index = node.getNode().getAttributes().getNamedItem("index").getNodeValue();
		String item = node.getNode().getAttributes().getNamedItem("item").getNodeValue();
		
		String index = null;
		Node  index_node = node.getNode().getAttributes().getNamedItem("index");
		if(index_node!=null){
			index =index_node.getNodeValue();
		}
		Node  open_node = node.getNode().getAttributes().getNamedItem("open");
		String open = null;
		if(open_node!=null){
			open =open_node.getNodeValue();
		}
		Node  separator_node = node.getNode().getAttributes().getNamedItem("separator");
		String separator = null;
		if(separator_node!=null){
			separator =separator_node.getNodeValue();
		}
		Node  close_node = node.getNode().getAttributes().getNamedItem("close");
		String close = null;
		if(close_node!=null){
			close =close_node.getNodeValue();
		}
		Object bind_collection;
		//int bind_indexaa = 0;
		try {
			bind_collection = OgnlCache.getValue(collection, map_backup);
			//String bind_item = (String) OgnlCache.getValue(item, map_backup);
			//String bind_index = (String) OgnlCache.getValue(index, map_backup);
			//bind_indexaa = bind_indexaa+Integer.parseInt(bind_index);
		} catch (OgnlException e) {
			 throw new ParseConfigException(e);
		}
		if(bind_collection instanceof java.util.Collection){
			Collection coll = (Collection) bind_collection;
			int n = 0;
			
			for(Object obj:coll){
				if(n<1){
					StringBuilder xql = context.getXqlBuilder();
					if(StringUtils.isNotBlank(open)){
						xql.append(open);
					}
					map_backup.put(item, obj);
					DynamicXQLSource.parseOne(node, context);
					n++;
					map_backup.remove(item);
				}else{
					StringBuilder xql = context.getXqlBuilder();
					if(StringUtils.isNotBlank(separator)){
						xql.append(separator);
					}
					map_backup.put(item, obj);
					DynamicXQLSource.parseOne(node, context);
					n++;
					map_backup.remove(item);
				}
			}
			StringBuilder xql = context.getXqlBuilder();
		 
			if(StringUtils.isNotBlank(close)){
				xql.append(close);
			}
		}
		//map.put("ids", ids);
		//map.put("index", index);
		//map.put("item", item);
		//context.setBindings((ContextMap) map);
		//DynamicXQLSource.parseOne(node, context);
		//context.setBindings((ContextMap) map_backup);
		
		
		
		return false;
	}

}
