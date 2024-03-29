package overcloud.blog.usecase.blog.common;

import overcloud.blog.infrastructure.validation.Error;

public enum TagError implements Error {

    TAG_EXISTS("tag.exists", "Tag existed!"),
    TAG_NO_EXISTS("tag.no-exists", "These tags doesn't exist: %s!");

    private String messageId;

    private String errorMessage;

    TagError(String messageId, String errorMessage) {
        this.messageId = messageId;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public void setMessageId(String id) {
        this.messageId = id;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
