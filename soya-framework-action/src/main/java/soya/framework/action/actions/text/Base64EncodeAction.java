package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;

import java.util.Base64;

@ActionDefinition(
        domain = "text-utils",
        name = "base64encode"
)
public class Base64EncodeAction extends TextUtilAction {

    @Override
    public String execute() throws Exception {
        return Base64.getEncoder().encodeToString(text.getBytes(getEncoding()));
    }

}
