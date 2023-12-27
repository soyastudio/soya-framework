package soya.framework.action.dsl;

import java.util.ArrayList;
import java.util.List;

public class ActionPhrase {

    private final String keyword;
    private List<String> objects;

    public ActionPhrase(String keyword) {
        this.keyword = keyword;
        this.objects = new ArrayList<>();
    }

    public String getKeyword() {
        return keyword;
    }

    public List<String> getObjects() {
        return objects;
    }
}
