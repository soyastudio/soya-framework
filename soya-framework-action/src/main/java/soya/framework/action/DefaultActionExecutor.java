package soya.framework.action;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

public class DefaultActionExecutor implements ActionExecutor {
    private ActionContext actionContext;

    public DefaultActionExecutor(ActionContext actionContext) {
        this.actionContext = actionContext;
    }

    @Override
    public Object execute(String command) {
        Action action = null;
        StringTokenizer tokenizer = new StringTokenizer(command);
        while(tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if(action == null) {
                try {
                    ActionName actionName = ActionName.fromURI(new URI(token));
                    action = ActionClass.forName(actionName).newInstance(actionContext);



                    System.out.println("==================== " + action.getInputParameterName());

                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        return "Commandline";
    }


}
