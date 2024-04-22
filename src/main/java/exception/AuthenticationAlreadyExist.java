package exception;

import static com.ruby.mall.constant.StatusCode.AUTHENTICATION_ALREADY_EXIST;

public class AuthenticationAlreadyExist extends Exception{
    public AuthenticationAlreadyExist(){
        super(AUTHENTICATION_ALREADY_EXIST.getResponseBody());
    }
}
