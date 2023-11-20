package soya.framework.commons.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class BeanUtils {
    private BeanUtils() {
    }

    public static Map<String, Object> toMap(DynaBean<?> dynaBean) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (DynaProperty property : dynaBean.getDynaClass().getDynaProperties()) {
            Class<?> propType = property.getType();
            Object value = dynaBean.get(property.getName());
            if(DynaBean.class.isAssignableFrom(propType)) {
                map.put(property.getName(), toMap((DynaBean<?>) value));
            } else {
                map.put(property.getName(), value);
            }
        }

        return map;
    }

    public static <T> T toObject(DynaBean<?> bean, Class<T> type) {
        return JsonUtils.fromJson(JsonUtils.toJson(bean), type);
    }
}
