package soya.framework.action.actions.text;

import java.util.Base64;

public class Base64DecodeAction extends TextUtilAction {

    @Override
    public String execute() throws Exception {
        return new String(Base64.getDecoder().decode(text.getBytes()), encoding);
    }
}
