package overcloud.blog.infrastructure;

import overcloud.blog.infrastructure.validation.Error;

public enum AuthError implements Error {
    AUTHORIZE_FAILED("authorize.failed", "Authorize failed!");

    private String messageId;

    private String errorMessage;

    AuthError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}