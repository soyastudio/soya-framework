package soya.framework.metadata;

public final class MetadataUtils {
    private MetadataUtils() {
    }

    public static boolean isAnnotatedAs(Class cls, Class annotationType) {
        Class<?> parent = cls;
        while (!parent.equals(Object.class)) {
            if(parent.getAnnotation(annotationType) != null) {
                return true;
            } else {

            }

            parent = parent.getSuperclass();
        }

        return false;
    }
}
