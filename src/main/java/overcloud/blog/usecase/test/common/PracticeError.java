package overcloud.blog.usecase.test.common;


import overcloud.blog.infrastructure.validation.Error;


public enum PracticeError implements Error {
    PRACTICE_NOT_FOUND("practice.not-found", "Không tồn tại phần thực hành này!"),
    ;

    private String id;

    private String errorMessage;

    PracticeError(String id, String errorMessage) {
        this.id = id;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessageId() {
        return id;
    }

    @Override
    public void setMessageId(String id) {
        this.id = id;
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
