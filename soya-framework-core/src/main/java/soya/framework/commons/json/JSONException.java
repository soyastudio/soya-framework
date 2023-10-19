package soya.framework.commons.json;

public class JSONException extends RuntimeException {

    public JSONException(final String message) {
        super(message);
    }


    public JSONException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public JSONException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
