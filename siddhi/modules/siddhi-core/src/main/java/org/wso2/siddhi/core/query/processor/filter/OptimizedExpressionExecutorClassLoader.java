package org.wso2.siddhi.core.query.processor.filter;

public class OptimizedExpressionExecutorClassLoader extends ClassLoader{
    public Class defineClass(String name, byte[] b){
        return defineClass(name,b,0,b.length);
    }
}
