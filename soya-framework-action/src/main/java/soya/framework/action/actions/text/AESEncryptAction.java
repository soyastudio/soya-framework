package soya.framework.action.actions.text;

public class AESEncryptAction extends AESAction {

    @Override
    public String execute() throws Exception {
        return encrypt(text, secret);
    }
}
