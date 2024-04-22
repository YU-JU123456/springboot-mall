package exception;

import static com.ruby.mall.constant.StatusCode.AUTHENTICATION_ROLE_ILLEGAL;

public class AuthenticationRoleillegle extends Exception{
    public  AuthenticationRoleillegle(){
        super(AUTHENTICATION_ROLE_ILLEGAL.getResponseBody());
    }
}
