package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mian on 2017/1/27.
 * 结果处理工具类
 */
public class ResultUtils {
    private static Logger logger = LoggerFactory.getLogger(ResultUtils.class);

    public static List<Map<String, Object>> splitMapList(Map<String, Object> mapList){
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            if (mapList.values().iterator().next() instanceof ArrayList) {
                int arrLength = ((ArrayList) mapList.values().iterator().next()).size();
                Object[] keys = mapList.keySet().toArray();

                for (int i = 0; i < arrLength; i++) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (Object key : keys) {
                        ArrayList list = (ArrayList) mapList.get(key);
                        map.put(key.toString(), list.get(i));
                    }

                    results.add(map);
                }
            }
        }catch (Exception e){
            logger.warn("mapList转ListMap失败，value长度不一致！", e);
        }

        return results;
    }
}
