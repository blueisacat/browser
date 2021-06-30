package cn.gc.utils;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * @author : gc
 * @date : 2021/6/25
 */
public class PropertiesUtils {

	private PropertiesUtils() {

	}

	private static class Static {
		private final static PropertiesUtils instance = new PropertiesUtils();
	}

	private Map<String, Object> properties = Maps.newHashMap();

	private Map<String, Object> cache = Maps.newHashMap();

	public static PropertiesUtils getInstance() {
		if (Static.instance.properties.isEmpty()) {
			Static.instance.load();
		}
		return Static.instance;
	}

	@SuppressWarnings("unchecked")
	public Object get(String path) {
		if (cache.containsKey(path)) {
			return cache.get(path);
		}
		String[] keys = path.split("\\.");
		Map<String, Object> map = properties;
		for (int i = 0; i < keys.length; i++) {
			if (map.get(keys[i]) instanceof Map) {
				map = (Map<String, Object>) map.get(keys[i]);
			} else {
				if (keys.length == i + 1) {
					cache.put(path, map.get(keys[i]));
					return map.get(keys[i]);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public String getStringVal(String path) {
		Object obj = get(path);
		if (null != obj) {
			return String.valueOf(obj);
		} else {
			return null;
		}
	}

	public Integer getIntegerVal(String path) {
		Object obj = get(path);
		if (null != obj) {
			return Integer.parseInt(getStringVal(path));
		} else {
			return null;
		}
	}
	
	public Boolean getBooleanVal(String path) {
		Object obj = get(path);
		if (null != obj) {
			return Boolean.valueOf(getStringVal(path));
		} else {
			return null;
		}
	}

	public void load() {
		Yaml yaml = new Yaml();
		try {
			// 读取当前项目同级目录下的配置文件,jar包形式
			File propertiesFile = new File(System.getProperty("user.dir"), "browser.yml");
			if (!propertiesFile.exists()) {
				// 读取source目录下配置文件
				URL url = PropertiesUtils.class.getClassLoader().getResource("browser.yml");
				if (url == null) {
					throw new RuntimeException("not found browser.yml");
				}
				propertiesFile = new File(url.getPath());
			}
			properties = yaml.load(FileUtils.openInputStream(propertiesFile));
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}