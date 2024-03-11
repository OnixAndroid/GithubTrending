package com.app.githubtrending.ui.common;

public sealed class ErrorMessage permits ErrorMessage.ApiLimit, ErrorMessage.HttpError, ErrorMessage.NoError, ErrorMessage.UnknownError {

    public static final class ApiLimit extends ErrorMessage { }

    public static final class HttpError extends ErrorMessage {
        private final int code;

        public HttpError(int code) {
            this.code = code;
        }


        public int getCode() {
            return code;
        }
    }

    public static final class UnknownError extends ErrorMessage { }
    public static final class NoError extends ErrorMessage { }
}
