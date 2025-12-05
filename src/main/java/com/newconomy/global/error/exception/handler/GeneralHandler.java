package com.newconomy.global.error.exception.handler;

import com.newconomy.global.error.exception.GeneralException;
import com.newconomy.global.response.BaseErrorCode;

public class GeneralHandler extends GeneralException {
    public GeneralHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
