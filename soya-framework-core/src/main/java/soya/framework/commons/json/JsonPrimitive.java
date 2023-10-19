package soya.framework.commons.json;

import java.util.Objects;

public class JsonPrimitive extends JSON {
    private final Object value;

    protected JsonPrimitive(Boolean value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    protected JsonPrimitive(Number value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    protected JsonPrimitive(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean booleanValue() {
        if (isBoolean()) {
            return ((Boolean) value).booleanValue();

        } else if (isNumber()) {
            return ((Number) value).intValue() != 0;

        } else {
            return Boolean.parseBoolean(value.toString().toLowerCase());
        }
    }

    public double doubleValue() {
        if (isNumber()) {
            return ((Number) value).doubleValue();

        } else if (isString()) {
            try {
                return Double.parseDouble(value.toString());

            } catch (NumberFormatException e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Primitive is neither a number nor a string");
        }

    }

    public double floatValue() {
        if (isNumber()) {
            return ((Number) value).floatValue();

        } else if (isString()) {
            try {
                return Float.parseFloat(value.toString());

            } catch (NumberFormatException e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Primitive is neither a number nor a string");
        }

    }

    public long longValue() {
        if (isNumber()) {
            return ((Number) value).longValue();

        } else if (isString()) {
            try {
                return Long.parseLong(value.toString());

            } catch (NumberFormatException e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Primitive is neither a number nor a string");
        }
    }

    public int intValue() {
        if (isNumber()) {
            return ((Number) value).intValue();

        } else if (isString()) {
            try {
                return Integer.parseInt(value.toString());

            } catch (NumberFormatException e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Primitive is neither a number nor a string");
        }
    }

    public short shortValue() {
        if (isNumber()) {
            return ((Number) value).shortValue();

        } else if (isString()) {
            try {
                return Short.parseShort(value.toString());

            } catch (NumberFormatException e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Primitive is neither a number nor a string");
        }
    }

    public String stringValue() {
        return value.toString();
    }

    public char charValue() {
        String s = value.toString();
        if (s.isEmpty()) {
            throw new JSONException("String value is empty");
        } else {
            return s.charAt(0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonPrimitive)) return false;
        JsonPrimitive that = (JsonPrimitive) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
