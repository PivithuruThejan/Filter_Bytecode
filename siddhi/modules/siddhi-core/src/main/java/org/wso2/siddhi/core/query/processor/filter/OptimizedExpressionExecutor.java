/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.executor.ConstantExpressionExecutor;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.VariableExpressionExecutor;
import org.wso2.siddhi.core.executor.condition.AndConditionExpressionExecutor;
import org.wso2.siddhi.core.executor.condition.NotConditionExpressionExecutor;
import org.wso2.siddhi.core.executor.condition.OrConditionExpressionExecutor;
import org.wso2.siddhi.core.executor.condition.compare.greaterthan.GreaterThanCompareConditionExpressionExecutorFloatDouble;
import org.wso2.siddhi.core.executor.condition.compare.lessthan.LessThanCompareConditionExpressionExecutorFloatDouble;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static jdk.internal.org.objectweb.asm.Opcodes.*;


/**
 * Implementation of the Siddhi Expression Executor in an optimized manner
 *
 *
 */
public class OptimizedExpressionExecutor {
    /**
     * Takes data from the complex event object and returns whether that event will proceed or not
     * @param complexEvent
     * @return
     */
    static  boolean count = true;
    static  byte[] byteArray;
    static AbstractOptimizedExpressionExecutor abstractOptimizedExpressionExecutor;

    public static boolean test(ExpressionExecutor expressionExecutor , ComplexEvent complexEvent) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        OptimizedExpressionExecutorClassLoader optimizedExpressionExecutorClassLoader = new OptimizedExpressionExecutorClassLoader();
        Class regeneratedClass = optimizedExpressionExecutorClassLoader.defineClass("ByteCode",byteArray);
        AbstractOptimizedExpressionExecutor object = (AbstractOptimizedExpressionExecutor) regeneratedClass.newInstance();
        boolean result = object.optimizedExecuteWithByteCode(expressionExecutor, complexEvent);
        //System.out.println("Byte code executor");
        return result;
    }




    public boolean optimizedExecute(ExpressionExecutor conditionExecutor, ComplexEvent complexEvent){

        if(conditionExecutor instanceof AndConditionExpressionExecutor){
            //System.out.println("AND");
            ExpressionExecutor left = ((AndConditionExpressionExecutor) conditionExecutor).getLeftConditionExecutor();
            ExpressionExecutor right = ((AndConditionExpressionExecutor) conditionExecutor).getRightConditionExecutor();
            return optimizedExecute(left,complexEvent) && optimizedExecute(right,complexEvent);

        }else if(conditionExecutor instanceof OrConditionExpressionExecutor){
            //System.out.println("OR");
            ExpressionExecutor left = ((OrConditionExpressionExecutor) conditionExecutor).getLeftConditionExecutor();
            ExpressionExecutor right = ((OrConditionExpressionExecutor) conditionExecutor).getRightConditionExecutor();
            return optimizedExecute(left,complexEvent) || optimizedExecute(right,complexEvent);
        }else if(conditionExecutor instanceof NotConditionExpressionExecutor){
            //System.out.println("NOT");
            ExpressionExecutor condition = ((NotConditionExpressionExecutor) conditionExecutor).getConditionExecutor();
            return !optimizedExecute(condition,complexEvent);

        }else if(conditionExecutor instanceof GreaterThanCompareConditionExpressionExecutorFloatDouble){
            //System.out.println(">");
            ExpressionExecutor left = ((GreaterThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getLeftExpressionExecutor();
            ExpressionExecutor right = ((GreaterThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getRightExpressionExecutor();
            Float leftVariable = null;
            Double rightVariable = null;

            if(left instanceof VariableExpressionExecutor){
                leftVariable = (Float) complexEvent.getAttribute(((VariableExpressionExecutor) left).getPosition());
            }else if(left instanceof ConstantExpressionExecutor){
                leftVariable = (Float) ((ConstantExpressionExecutor) left).getValue();
            }

            if(right instanceof VariableExpressionExecutor){
                rightVariable = (Double) complexEvent.getAttribute(((VariableExpressionExecutor) right).getPosition());
            }else if(right instanceof  ConstantExpressionExecutor){
                rightVariable = (Double) ((ConstantExpressionExecutor) right).getValue();
            }
            return leftVariable > rightVariable;

        }else if(conditionExecutor instanceof LessThanCompareConditionExpressionExecutorFloatDouble){
            //System.out.println("<");
            ExpressionExecutor left = ((LessThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getLeftExpressionExecutor();
            ExpressionExecutor right = ((LessThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getRightExpressionExecutor();
            Float leftVariable = null;
            Double rightVariable = null;

            if(left instanceof VariableExpressionExecutor){
                leftVariable = (Float) complexEvent.getAttribute(((VariableExpressionExecutor) left).getPosition());
            }else if(left instanceof ConstantExpressionExecutor){
                leftVariable = (Float) ((ConstantExpressionExecutor) left).getValue();
            }

            if(right instanceof VariableExpressionExecutor){
                rightVariable = (Double) complexEvent.getAttribute(((VariableExpressionExecutor) right).getPosition());
            }else if(right instanceof  ConstantExpressionExecutor){
                rightVariable = (Double) ((ConstantExpressionExecutor) right).getValue();
            }
            return leftVariable < rightVariable;

        }else{
            return true;
        }

        //return true;
    }

    public static boolean byteCode(ExpressionExecutor expressionExecutor , ComplexEvent complexEvent) throws IllegalAccessException, InvocationTargetException, InstantiationException{

        /*OptimizedExpressionExecutorClassLoader optimizedExpressionExecutorClassLoader = new OptimizedExpressionExecutorClassLoader();
        Class regeneratedClass = optimizedExpressionExecutorClassLoader.defineClass("ByteCode",byteArray);
        AbstractOptimizedExpressionExecutor abstractOptimizedExpressionExecutor = (AbstractOptimizedExpressionExecutor) regeneratedClass.newInstance();
        boolean result = abstractOptimizedExpressionExecutor.optimizedExecuteWithByteCode(expressionExecutor, complexEvent);*/
        System.out.println("From Byte Code");
        return true;

    }





    public boolean newWay(ExpressionExecutor expressionExecutor , ComplexEvent complexEvent) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor methodVisitor;
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

            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", null, null);
            methodVisitor.visitCode();

            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ISTORE, 3);

            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ISTORE, 4);


            float f = (float) 1.7;
            methodVisitor.visitLdcInsn(new Float(f));
            methodVisitor.visitVarInsn(FSTORE, 5);

            double d = 1.5;
            methodVisitor.visitLdcInsn(new Double(d));
            methodVisitor.visitVarInsn(DSTORE, 6);

            methodVisitor.visitVarInsn(FLOAD, 5);
            methodVisitor.visitInsn(F2D);
            methodVisitor.visitVarInsn(DLOAD, 6);
            methodVisitor.visitInsn(DCMPL);
            Label l0 = new Label();
            methodVisitor.visitJumpInsn(IFLE, l0);
            methodVisitor.visitInsn(ICONST_1);
            Label l1 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l1);
            methodVisitor.visitLabel(l0);
            methodVisitor.visitFrame(Opcodes.F_FULL, 7, new Object[]{"org/wso2/siddhi/core/query/processor/filter/Test", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/event/ComplexEvent", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.DOUBLE}, 0, new Object[]{});
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(l1);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitVarInsn(ISTORE, 3);

            methodVisitor.visitVarInsn(ILOAD, 3);
            methodVisitor.visitInsn(IRETURN);

            methodVisitor.visitMaxs(4, 8);
            methodVisitor.visitEnd();

        }
        classWriter.visitEnd();
        byte[] newByteCode = classWriter.toByteArray();
        OptimizedExpressionExecutorClassLoader optimizedExpressionExecutorClassLoader = new OptimizedExpressionExecutorClassLoader();
        Class regeneratedClass = optimizedExpressionExecutorClassLoader.defineClass("ByteCode",newByteCode);
        AbstractOptimizedExpressionExecutor abstractOptimizedExpressionExecutor = (AbstractOptimizedExpressionExecutor) regeneratedClass.newInstance();
        boolean result = abstractOptimizedExpressionExecutor.optimizedExecuteWithByteCode(expressionExecutor, complexEvent);
        System.out.println(result);
        return result;
    }




    public boolean execute(ExpressionExecutor expressionExecutor , ComplexEvent complexEvent) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor methodVisitor;
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

            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", null, null);
            methodVisitor.visitCode();

            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/condition/AndConditionExpressionExecutor");
            Label l0 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/AndConditionExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/AndConditionExpressionExecutor", "getLeftConditionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/AndConditionExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/AndConditionExpressionExecutor", "getRightConditionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "ByteCode", "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", false);
            Label l1 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "ByteCode", "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", false);
            methodVisitor.visitJumpInsn(IFEQ, l1);
            methodVisitor.visitInsn(ICONST_1);
            Label l2 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l2);
            methodVisitor.visitLabel(l1);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 2, new Object[]{"org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/executor/ExpressionExecutor"}, 0, null);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(l2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitLabel(l0);
            methodVisitor.visitFrame(Opcodes.F_CHOP, 2, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/condition/OrConditionExpressionExecutor");
            Label l3 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/OrConditionExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/OrConditionExpressionExecutor", "getLeftConditionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/OrConditionExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/OrConditionExpressionExecutor", "getRightConditionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "ByteCode", "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", false);
            Label l4 = new Label();
            methodVisitor.visitJumpInsn(IFNE, l4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "ByteCode", "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", false);
            Label l5 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l5);
            methodVisitor.visitLabel(l4);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 2, new Object[]{"org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/executor/ExpressionExecutor"}, 0, null);
            methodVisitor.visitInsn(ICONST_1);
            Label l6 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l6);
            methodVisitor.visitLabel(l5);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(l6);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitLabel(l3);
            methodVisitor.visitFrame(Opcodes.F_CHOP, 2, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/condition/NotConditionExpressionExecutor");
            Label l7 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l7);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/NotConditionExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/NotConditionExpressionExecutor", "getConditionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "ByteCode", "optimizedExecuteWithByteCode", "(Lorg/wso2/siddhi/core/executor/ExpressionExecutor;Lorg/wso2/siddhi/core/event/ComplexEvent;)Z", false);
            Label l8 = new Label();
            methodVisitor.visitJumpInsn(IFNE, l8);
            methodVisitor.visitInsn(ICONST_1);
            Label l9 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l9);
            methodVisitor.visitLabel(l8);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"org/wso2/siddhi/core/executor/ExpressionExecutor"}, 0, null);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(l9);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitLabel(l7);
            methodVisitor.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/condition/compare/greaterthan/GreaterThanCompareConditionExpressionExecutorFloatDouble");
            Label l10 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l10);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/compare/greaterthan/GreaterThanCompareConditionExpressionExecutorFloatDouble");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/compare/greaterthan/GreaterThanCompareConditionExpressionExecutorFloatDouble", "getLeftExpressionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/compare/greaterthan/GreaterThanCompareConditionExpressionExecutorFloatDouble");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/compare/greaterthan/GreaterThanCompareConditionExpressionExecutorFloatDouble", "getRightExpressionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            Label l11 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l11);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/VariableExpressionExecutor", "getPosition", "()[I", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/wso2/siddhi/core/event/ComplexEvent", "getAttribute", "([I)Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
            methodVisitor.visitVarInsn(ASTORE, 5);
            Label l12 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l12);
            methodVisitor.visitLabel(l11);
            methodVisitor.visitFrame(Opcodes.F_FULL, 7, new Object[]{"ByteCode", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/event/ComplexEvent", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/executor/ExpressionExecutor", "java/lang/Float", "java/lang/Double"}, 0, new Object[]{});
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitJumpInsn(IFEQ, l12);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor", "getValue", "()Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitLabel(l12);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            Label l13 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l13);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/VariableExpressionExecutor", "getPosition", "()[I", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/wso2/siddhi/core/event/ComplexEvent", "getAttribute", "([I)Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
            methodVisitor.visitVarInsn(ASTORE, 6);
            Label l14 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l14);
            methodVisitor.visitLabel(l13);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitJumpInsn(IFEQ, l14);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor", "getValue", "()Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitLabel(l14);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
            methodVisitor.visitInsn(F2D);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
            methodVisitor.visitInsn(DCMPL);
            Label l15 = new Label();
            methodVisitor.visitJumpInsn(IFLE, l15);
            methodVisitor.visitInsn(ICONST_1);
            Label l16 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l16);
            methodVisitor.visitLabel(l15);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(l16);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitLabel(l10);
            methodVisitor.visitFrame(Opcodes.F_FULL, 3, new Object[]{"ByteCode", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/event/ComplexEvent"}, 0, new Object[]{});
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/condition/compare/lessthan/LessThanCompareConditionExpressionExecutorFloatDouble");
            Label l17 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l17);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/compare/lessthan/LessThanCompareConditionExpressionExecutorFloatDouble");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/compare/lessthan/LessThanCompareConditionExpressionExecutorFloatDouble", "getLeftExpressionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/condition/compare/lessthan/LessThanCompareConditionExpressionExecutorFloatDouble");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/condition/compare/lessthan/LessThanCompareConditionExpressionExecutorFloatDouble", "getRightExpressionExecutor", "()Lorg/wso2/siddhi/core/executor/ExpressionExecutor;", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            Label l18 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l18);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/VariableExpressionExecutor", "getPosition", "()[I", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/wso2/siddhi/core/event/ComplexEvent", "getAttribute", "([I)Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
            methodVisitor.visitVarInsn(ASTORE, 5);
            Label l19 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l19);
            methodVisitor.visitLabel(l18);
            methodVisitor.visitFrame(Opcodes.F_FULL, 7, new Object[]{"ByteCode", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/event/ComplexEvent", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/executor/ExpressionExecutor", "java/lang/Float", "java/lang/Double"}, 0, new Object[]{});
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitJumpInsn(IFEQ, l19);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor", "getValue", "()Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitLabel(l19);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            Label l20 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, l20);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/VariableExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/VariableExpressionExecutor", "getPosition", "()[I", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/wso2/siddhi/core/event/ComplexEvent", "getAttribute", "([I)Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
            methodVisitor.visitVarInsn(ASTORE, 6);
            Label l21 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l21);
            methodVisitor.visitLabel(l20);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(INSTANCEOF, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitJumpInsn(IFEQ, l21);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/executor/ConstantExpressionExecutor", "getValue", "()Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitLabel(l21);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
            methodVisitor.visitInsn(F2D);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
            methodVisitor.visitInsn(DCMPG);
            Label l22 = new Label();
            methodVisitor.visitJumpInsn(IFGE, l22);
            methodVisitor.visitInsn(ICONST_1);
            Label l23 = new Label();
            methodVisitor.visitJumpInsn(GOTO, l23);
            methodVisitor.visitLabel(l22);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitLabel(l23);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitLabel(l17);
            methodVisitor.visitFrame(Opcodes.F_FULL, 3, new Object[]{"ByteCode", "org/wso2/siddhi/core/executor/ExpressionExecutor", "org/wso2/siddhi/core/event/ComplexEvent"}, 0, new Object[]{});
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitMaxs(4, 7);
            methodVisitor.visitEnd();

        }

        byteArray = classWriter.toByteArray();
        count = false;

        OptimizedExpressionExecutorClassLoader optimizedExpressionExecutorClassLoader = new OptimizedExpressionExecutorClassLoader();
        Class regeneratedClass = optimizedExpressionExecutorClassLoader.defineClass("ByteCode",byteArray);
        AbstractOptimizedExpressionExecutor abstractOptimizedExpressionExecutor = (AbstractOptimizedExpressionExecutor) regeneratedClass.newInstance();
        boolean result = abstractOptimizedExpressionExecutor.optimizedExecuteWithByteCode(expressionExecutor, complexEvent);
        //System.out.println("generator");

        return result;

    }

    public static void trial(ExpressionExecutor conditionExecutor , ComplexEvent complexEvent){
        StreamEvent streamEvent = (StreamEvent) complexEvent;
        Object[] eventData  = streamEvent.getBeforeWindowData();


    }






}
