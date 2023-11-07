package soya.framework.action.actions.text;

import java.util.Base64;

public class Base64EncodeAction extends TextUtilAction {

    @Override
    public String execute() throws Exception {
        return Base64.getEncoder().encodeToString(text.getBytes(encoding));
    }

}
