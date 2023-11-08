package soya.framework.restruts.reflect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.restruts.Action;

public abstract class ReflectAction extends Action {
    protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
