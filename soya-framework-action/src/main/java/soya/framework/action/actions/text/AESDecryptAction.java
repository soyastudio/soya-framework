package soya.framework.action.actions.text;

public class AESDecryptAction extends AESAction {

    @Override
    public String execute() throws Exception {
        return decrypt(text, secret);
    }
}
