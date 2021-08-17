package cn.blueisacat.utils;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author : gc
 * @date : 2021/4/19
 */
public class ConfigUtils {

    private ConfigUtils() {

    }

    private static ConfigUtils instance = new ConfigUtils();

    public static ConfigUtils getInstance() {
        return instance;
    }

    static {
        getInstance().init();
    }

    private Map<String, Object> configMap = Maps.newHashMap();

    private Map<String, Object> configMapCache = Maps.newHashMap();

    public Object get(String path) {
        if (configMapCache.containsKey(path)) {
            return configMapCache.get(path);
        }
        String[] keys = path.split("\\.");
        Object config = find(keys, 0, configMap);
        if (null == config) {
            return null;
        } else {
            configMapCache.put(path, config);
            return config;
        }
    }

    private Object find(String[] keys, int index, Map<String, Object> configMap) {
        String key = keys[index];
        if (configMap.containsKey(key)) {
            Object config = configMap.get(key);
            if (keys.length == index + 1) {
                return config;
            } else {
                return find(keys, index + 1, (Map<String, Object>) config);
            }
        } else {
            return null;
        }
    }

    public String getStringVal(String path) {
        Object obj = get(path);
        if (null != obj && obj instanceof String) {
            return (String) obj;
        } else {
            return null;
        }
    }

    public Integer getIntegerVal(String path) {
        Object obj = get(path);
        if (null != obj && obj instanceof Integer) {
            return (Integer) obj;
        } else {
            return null;
        }
    }

    public Boolean getBooleanVal(String path) {
        Object obj = get(path);
        if (null != obj && obj instanceof Boolean) {
            return (Boolean) obj;
        } else {
            return null;
        }
    }

    public List getListVal(String path) {
        Object obj = get(path);
        if (null != obj && obj instanceof List) {
            return (List) obj;
        } else {
            return null;
        }
    }

    public Map getMapVal(String path) {
        Object obj = get(path);
        if (null != obj && obj instanceof Map) {
            return (Map) obj;
        } else {
            return null;
        }
    }

    private void init() {
        Yaml yaml = new Yaml();
        try {
            // 读取自定义配置文件
            File configFile = new File(System.getProperty("browser.config.location", ""));
            if (!configFile.exists() || configFile.isDirectory()) {
                // 读取当前项目同级目录下的配置文件
                configFile = new File(System.getProperty("user.dir", ""), "browser.yml");
                if (!configFile.exists()) {
                    // 读取当前项目同级目录下config文件夹下的配置文件
                    configFile = new File(System.getProperty("user.dir", "") + File.separator + "config", "browser.yml");
                    if (!configFile.exists()) {
                        // 读取source目录下配置文件
                        URL url = ConfigUtils.class.getClassLoader().getResource("browser.yml");
                        if (url == null) {
                            throw new RuntimeException("not found browser.yml");
                        }
                        configFile = new File(url.getPath());
                    }
                }
            }
            configMap = yaml.load(FileUtils.openInputStream(configFile));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}