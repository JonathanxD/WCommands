package com.github.jonathanxd.wcommands.ext.reflect.processor.exception;

/**
 * Created by jonathan on 11/03/16.
 */
public class PossibleCyclicDependencies extends RuntimeException {
    public PossibleCyclicDependencies() {
        super();
    }

    public PossibleCyclicDependencies(String message) {
        super(message);
    }

    public PossibleCyclicDependencies(String message, Throwable cause) {
        super(message, cause);
    }

    public PossibleCyclicDependencies(Throwable cause) {
        super(cause);
    }

    protected PossibleCyclicDependencies(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
