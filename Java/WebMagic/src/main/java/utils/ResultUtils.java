package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;

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

    public static String IS_MULTI_ITEMS_STR = "isMultiItems";
    public static String MULTI_ITEMS_STR = "multiItems";

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

    public static List<Map<String, Object>> getResultItemList(ResultItems resultItems, boolean isMultiItems){
        List<Map<String, Object>> results;
        if (isMultiItems) {
            results = splitMapList(resultItems.getAll());
            if (results == null || results.size() == 0) {
                results = new ArrayList<>();
                results.add(resultItems.getAll());
            }
        } else {
            results = new ArrayList<>();
            results.add(resultItems.getAll());
        }

        return results;
    }
}
