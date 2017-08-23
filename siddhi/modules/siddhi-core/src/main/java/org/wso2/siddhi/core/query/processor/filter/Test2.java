/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* WSO2 Inc. licenses this file to you under the Apache License,
* Version 2.0 (the "License"); you may not use this file except
* in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.siddhi.core.query.processor.filter;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static jdk.internal.org.objectweb.asm.Opcodes.*;
import static jdk.internal.org.objectweb.asm.Opcodes.RETURN;

/**
 *consists of method to generate and execute byte code
 */
public class Test2 {
    public static ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);;
    public  static MethodVisitor methodVisitor;

    /**
     * creates a class called "Bytecode" which has an overridden method "optimizedExecuteWithByteCode".
     */
    public  void start(){
        OptimizedExpressionExecutor.count = false;
        //System.out.println("start");
        classWriter.visit(52,ACC_PUBLIC+ACC_SUPER,"ByteCode",null,"java/lang/Object",new String[]{"org/wso2/siddhi/core/query/processor/filter/AbstractOptimizedExpressionExecutor"});
        classWriter.visitSource("ByteCode.java",null);
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC,"<init>","()V",null,null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD,0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL,"java/lang/Object","<init>","()V",false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1,1);
            methodVisitor.visitEnd();
        }
        {
            Test2.methodVisitor = Test2.classWriter.visitMethod(ACC_PUBLIC, "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", null, null);
            Test2.methodVisitor.visitInsn(ICONST_0);
            Test2.methodVisitor.visitVarInsn(ISTORE, 3);

            Test2.methodVisitor.visitInsn(ICONST_0);
            Test2.methodVisitor.visitVarInsn(ISTORE, 4);
        }



    }

    /**
     * generates byte array from classWriter
     * @throws IllegalAccessException
     * @throws InstantiationException
     */

    public  void end() throws IllegalAccessException, InstantiationException, IOException {
        //System.out.println("end");
        Test2.methodVisitor.visitVarInsn(ILOAD, 3);
        Test2.methodVisitor.visitInsn(IRETURN);
        Test2.methodVisitor.visitMaxs(4, 8);
        Test2.methodVisitor.visitEnd();
        classWriter.visitEnd();
        OptimizedExpressionExecutor.byteArray = classWriter.toByteArray();
        OptimizedExpressionExecutorClassLoader optimizedExpressionExecutorClassLoader = new OptimizedExpressionExecutorClassLoader();
        Class regeneratedClass = optimizedExpressionExecutorClassLoader.defineClass("ByteCode",OptimizedExpressionExecutor.byteArray);
        OptimizedExpressionExecutor.abstractOptimizedExpressionExecutor = (AbstractOptimizedExpressionExecutor) regeneratedClass.newInstance();
        /*FileOutputStream output = new FileOutputStream(new File("SimpleByteCode.class"));
        output.write(OptimizedExpressionExecutor.byteArray);
        output.flush();*/

    }

    /**
     * method that executes bytecode
     * @param expressionExecutor
     * @param complexEvent
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */

    public boolean execute(ExpressionExecutor expressionExecutor , ComplexEvent complexEvent) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean result = OptimizedExpressionExecutor.abstractOptimizedExpressionExecutor.optimizedExecuteWithByteCode(expressionExecutor, complexEvent);
        return result;

    }

}
