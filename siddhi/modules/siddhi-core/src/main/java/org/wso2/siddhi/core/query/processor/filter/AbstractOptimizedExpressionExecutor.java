package org.wso2.siddhi.core.query.processor.filter;

import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.executor.ExpressionExecutor;

import java.lang.reflect.InvocationTargetException;

public interface AbstractOptimizedExpressionExecutor {
    public  boolean optimizedExecuteWithByteCode(ExpressionExecutor expressionExecutor , ComplexEvent complexEvent) throws IllegalAccessException, InstantiationException, InvocationTargetException;
}
