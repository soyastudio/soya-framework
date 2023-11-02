package soya.framework.restruts.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.restruts.Serializer;

public class GsonSerializer implements Serializer {
    public static final String MEDIA_TYPE = "application/json";

    private Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public <T> byte[] serialize(T o) throws SerializationException {
        return GSON.toJson(o).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws SerializationException {
        return GSON.fromJson(new String(data), type);
    }
}
