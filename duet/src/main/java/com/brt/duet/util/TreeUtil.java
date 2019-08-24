package com.brt.duet.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class TreeUtil {
	/**
	 * @author 方杰
	 * @date 2018年11月24日
	 * @param jsonArray 待转换的jsonArray
	 * @param key 主键名称
	 * @param parentKey 父键名称
	 * @param parentId 头节点主键值
	 * @return JSONArray 转换成树结构的jsonArray
	 * @description 将jsonArray数组结构转换成树结构
	 */
	public static JSONArray jsonArrayToTree(JSONArray jsonArray, String key, String parentKey, String parentId) {
		if (jsonArray.isEmpty() || StringUtils.isBlank(key) || StringUtils.isBlank(parentKey)) {
			return null;
		}
		JSONArray result = new JSONArray();
		if (StringUtils.isBlank(parentId)) {
			parentId = "";
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject node = jsonArray.getJSONObject(i);
			String nodeParentId = "";
			if (node.containsKey(parentKey) && StringUtils.isNotBlank(node.getString(parentKey))) {
				nodeParentId = node.getString(parentKey);
			}
			if (parentId.equals(nodeParentId)) {
				jsonArray.remove(i);
				i--;
				result.add(node);
			}
		}		
		for (int i = 0; i < result.size(); i++) {
			JSONObject node = result.getJSONObject(i);
			if (StringUtils.isNotBlank(node.getString(key))) {
				JSONArray children = jsonArrayToTree(jsonArray, key, parentKey, node.getString(key));
				if (children != null && !children.isEmpty()) {
					node.put("children", children);
				}			
			}			
		}
		return result;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月8日
	 * @param jsonArray 待转换的jsonArray
	 * @param key 主键名称
	 * @param parentKey 父键名称
	 * @return JSONArray 转换成树结构的jsonArray
	 * @description 将jsonArray数组结构转换成树结构
	 */
	public static JSONArray jsonArrayToTree(JSONArray jsonArray, String key, String parentKey) {
		if (jsonArray == null || jsonArray.isEmpty() || StringUtils.isBlank(key) || StringUtils.isBlank(parentKey)) {
			return null;
		}
		JSONArray result = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject nodex = jsonArray.getJSONObject(i);
			boolean tag = true;
			for (int j = 0; j < jsonArray.size(); j++) {
				JSONObject nodey = jsonArray.getJSONObject(j);
				if (nodey.get(key).equals(nodex.get(parentKey))){
					JSONArray children = null;
					if (nodey.containsKey("children")) {
						children = nodey.getJSONArray("children");
					} else {
						children = new JSONArray();
					}
					children.add(nodex);
					nodey.put("children", children);
					tag = false;
					break;
				}
			}
			if (tag) {
				result.add(nodex);
			}
		}
		return result;
	}
	
	/**
	 * @author 方杰
	 * @date 2018年11月28日
	 * @param jsonArray
	 * @param key
	 * @return 排序过的jsonArray
	 * @description 对jsonArray进行排序
	 */
	public static JSONArray sortList(JSONArray jsonArray, String key) {
		if (jsonArray.isEmpty() || StringUtils.isBlank(key)) {
			return null;
		}
		JSONArray result = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
            	int aInt = 0;
            	int bInt = 0;
                try {
                    // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                	aInt = a.getInteger(key);
                	bInt = b.getInteger(key);
                } catch (JSONException e) {
                    // do something
                }
                return aInt - bInt;
                // if you want to change the sort order, simply use the following:
                // return -valA.compareTo(valB);
            }
        });
        for (int i = 0; i < jsonValues.size(); i++) {
        	result.add(jsonValues.get(i));
        }
        return result;
	}
	
	/**
	 * @author 方杰
	 * @date 2018年11月28日
	 * @param jsonArray
	 * @param key
	 * @param children
	 * @return 排序过的树
	 * @description 对树进行排序
	 */
	public static JSONArray sortTree(JSONArray jsonArray, String key, String children) {		
		if (jsonArray == null || jsonArray.isEmpty() || StringUtils.isBlank(key) || StringUtils.isBlank(children)) {
			return null;
		}
		JSONArray result = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
            	int aInt = 0;
            	int bInt = 0;
                try {
                    // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                	aInt = a.getInteger(key);
                	bInt = b.getInteger(key);
                } catch (JSONException e) {
                    // do something
                }
                return aInt - bInt;
                // if you want to change the sort order, simply use the following:
                // return -valA.compareTo(valB);
            }
        });
        for (int i = 0; i < jsonValues.size(); i++) {
        	JSONObject item = jsonValues.get(i);
        	if (item.containsKey(children) && !item.getJSONArray(children).isEmpty()) {
        		item.put(children, sortTree(item.getJSONArray(children), key, children));
        	}
        	result.add(item);
        }
        return result;

	}
	
	/**
	 * @author 方杰
	 * @date 2019年1月12日
	 * @param jsonArray tree
	 * @param children children的key值
	 * @return jsonArray list
	 * @description 将tree转换成list
	 */
	public static JSONArray treeToJSONArray(JSONArray jsonArray, String children) {
		if (jsonArray == null) {
			return null;
		}
		JSONArray result = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject node = jsonArray.getJSONObject(i);
			if (node.containsKey(children) && node.getJSONArray(children) != null) {
				result.addAll(treeToJSONArray(node.getJSONArray(children), children));
			}
			JSONObject newNode = new JSONObject();
			newNode = node;
			newNode.remove(children);
			result.add(newNode);
		}
		return result;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年4月4日
	 * @param jsonArray 树
	 * @param key 主键key
	 * @param childrenKey 子节点key
	 * @param parentKey 父节点key
	 * @param nodeKey 查询的节点主键值
	 * @return 路径 以/分割
	 * @description 查询节点在树的路径
	 */
	public static String path(JSONArray jsonArray, String key, String childrenKey, String parentKey, String nodeKey) {
		if (jsonArray == null || StringUtils.isBlank(nodeKey)) {
			return null;
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject node = jsonArray.getJSONObject(i);
			if (nodeKey.equals(node.getString(key))) {
				return nodeKey;
			} else if (node.containsKey(childrenKey) && node.get(childrenKey) != null) {
				String path = path(node.getJSONArray(childrenKey), key, childrenKey, parentKey, nodeKey);
				if (path != null && path.endsWith(nodeKey)) {
					return node.getString(key) + "/" + path;
				}
			}
		}
		return null;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月8日
	 * @param jsonArray 需要查找的树
	 * @param key 主键key
	 * @param childrenKey 子节点key
	 * @param nodeKey 需要查找的根节点
	 * @return 子节点Set集合 包含根节点
	 * @description 获取树上某个节点的子节点，包含该节点
	 */
	public static Set<String> getChildrenKeys(JSONArray jsonArray, String key, String childrenKey, String nodeKey){
		if (jsonArray == null) {
			return null;
		}
		Set<String> set = new HashSet<>();
		JSONObject node = getNode(jsonArray, key, childrenKey, nodeKey);
		if (node == null) {
			return null;
		}
		set.add(nodeKey);
		if (node.containsKey(childrenKey) && node.get(childrenKey) != null) {
			Set<String> keys = getKeys(node.getJSONArray(childrenKey), key, childrenKey);
			set.addAll(keys);
		}
		return set;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月8日
	 * @param jsonArray 树
	 * @param key 主键key
	 * @param childrenKey 子节点key
	 * @param nodeKey 需要查找的节点key值
	 * @return 节点
	 * @description 查找节点
	 */
	public static JSONObject getNode(JSONArray jsonArray, String key, String childrenKey, String nodeKey){
		if (jsonArray == null) {
			return null;
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject node = jsonArray.getJSONObject(i);
			if (nodeKey.equals(node.getString(key))) {
				return node;
			} else if (node.containsKey(childrenKey) && node.get(childrenKey) != null){
				JSONObject node2 = getNode(node.getJSONArray(childrenKey), key, childrenKey, nodeKey);
				if (node2 != null) {
					return node2;
				}
			}
		}
		return null;
	}
	
	/**
	 * @author 方杰
	 * @date 2019年7月8日
	 * @param jsonArray 树
	 * @param key 主键key
	 * @param childrenKey 子节点key
	 * @return key的Set集合
	 * @description 获取一棵树的key值集合
	 */
	public static Set<String> getKeys(JSONArray jsonArray, String key, String childrenKey){
		if (jsonArray == null) {
			return null;
		}
		Set<String> set = new HashSet<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject node = jsonArray.getJSONObject(i);
			if (node.containsKey(childrenKey) && node.get(childrenKey) != null) {
				Set<String> keys = getKeys(node.getJSONArray(childrenKey), key, childrenKey);
				set.addAll(keys);
			}
			set.add(node.getString(key));
		}
		return set;
	}
	
}