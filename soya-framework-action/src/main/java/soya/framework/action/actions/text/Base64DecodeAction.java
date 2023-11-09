package soya.framework.action.actions.text;

import soya.framework.action.ActionDefinition;

import java.util.Base64;

@ActionDefinition(
        domain = "text-utils",
        name = "base64decode"
)
public class Base64DecodeAction extends TextUtilAction {
    @Override
    public String execute() throws Exception {
        return new String(Base64.getDecoder().decode(text.getBytes()), encoding);
    }
}
