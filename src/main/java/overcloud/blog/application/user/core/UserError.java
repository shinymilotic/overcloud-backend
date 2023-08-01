package overcloud.blog.application.user.core;

import overcloud.blog.infrastructure.validation.Error;


public enum UserError implements Error {
    USER_NOT_FOUND("user.get-current-user.not-found","Bạn phải đăng nhập trước!"),
    USER_USERNAME_EXIST("user.username.exists","Tên tài khoản đã tồn tại!"),
    USER_EMAIL_EXIST("user.email.exists", "Email này đã được sử dụng!"),
    USER_EMAIL_NO_EXIST("user.email.no-exists", "Tên đăng nhập hoặc mật khẩu không tồn tại!");
    ;

    private String id;

    private String errorMessage;

    UserError(String id, String errorMessage) {
        this.id = id;
        this.errorMessage = errorMessage;
    }

    @Override
    public void setMessageId(String id) {
        this.id = id;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}