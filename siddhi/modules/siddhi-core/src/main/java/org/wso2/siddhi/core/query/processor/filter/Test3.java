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
import jdk.internal.org.objectweb.asm.Label;
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
import org.wso2.siddhi.core.util.SiddhiConstants;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

/**
 * consists a method that generate bytecode while traversing Siddhi filter tree.
 */
public class Test3 {
    /**
     *Dynamically generates bytecode while traversing Siddhi filter tree.
     * @param conditionExecutor
     * @param complexEvent
     * @param status
     * @return
     *
     */
    public boolean execute(ExpressionExecutor conditionExecutor , ComplexEvent complexEvent , int status){
        if(conditionExecutor instanceof AndConditionExpressionExecutor){
            //System.out.println("AND");
            ExpressionExecutor left = ((AndConditionExpressionExecutor) conditionExecutor).getLeftConditionExecutor();
            ExpressionExecutor right = ((AndConditionExpressionExecutor) conditionExecutor).getRightConditionExecutor();
            boolean leftResult;
            boolean rightResult;
            leftResult = execute(left, complexEvent, 1);
            Test2.methodVisitor.visitVarInsn(ILOAD, 3);
            Label l0 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFNE, l0);
            Test2.methodVisitor.visitInsn(ICONST_0);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            Label l1 = new Label();
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l0);
            rightResult = execute(right, complexEvent, 2);
            Test2.methodVisitor.visitVarInsn(ILOAD, 4);
            Label l2 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFNE, l2);
            Test2.methodVisitor.visitInsn(ICONST_0);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l2);
            Test2.methodVisitor.visitInsn(ICONST_1);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            Test2.methodVisitor.visitLabel(l1);
            return  leftResult && rightResult;

        }else if(conditionExecutor instanceof OrConditionExpressionExecutor){
            //System.out.println("OR");
            ExpressionExecutor left = ((OrConditionExpressionExecutor) conditionExecutor).getLeftConditionExecutor();
            ExpressionExecutor right = ((OrConditionExpressionExecutor) conditionExecutor).getRightConditionExecutor();
            boolean leftResult;
            boolean rightResult;
            leftResult = execute(left, complexEvent, 1);
            Test2.methodVisitor.visitVarInsn(ILOAD, 3);
            Label l0 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFEQ, l0);
            Test2.methodVisitor.visitInsn(ICONST_1);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            Label l1 = new Label();
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l0);
            rightResult = execute(right, complexEvent, 2);
            Test2.methodVisitor.visitVarInsn(ILOAD, 4);
            Label l2 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFEQ, l2);
            Test2.methodVisitor.visitInsn(ICONST_1);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l2);
            Test2.methodVisitor.visitInsn(ICONST_0);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            Test2.methodVisitor.visitLabel(l1);
            return leftResult || rightResult;
        }else if(conditionExecutor instanceof NotConditionExpressionExecutor){
            //System.out.println("NOT");
            ExpressionExecutor condition = ((NotConditionExpressionExecutor) conditionExecutor).getConditionExecutor();
            boolean result = execute(condition, complexEvent, 1);
            Test2.methodVisitor.visitVarInsn(ILOAD, 3);
            Label l0 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFNE, l0);
            Test2.methodVisitor.visitInsn(ICONST_1);
            Label l1 = new Label();
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l0);
            Test2.methodVisitor.visitInsn(ICONST_0);
            Test2.methodVisitor.visitLabel(l1);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            return !result;

        }else if(conditionExecutor instanceof GreaterThanCompareConditionExpressionExecutorFloatDouble){
            //System.out.println(">");
            StreamEvent streamEvent = (StreamEvent) complexEvent;
            Object[] eventData  = streamEvent.getBeforeWindowData();
            ExpressionExecutor left = ((GreaterThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getLeftExpressionExecutor();
            ExpressionExecutor right = ((GreaterThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getRightExpressionExecutor();
            Float leftVariable = null;
            Double rightVariable = null;
            int[] leftPosition = null;
            int[] rightPosition = null;
            int beforeWindowIndexLeft = 0;
            int beforeWindowIndexRight = 0;
            Test2.methodVisitor.visitCode();
            if(left instanceof VariableExpressionExecutor){
                leftPosition = ((VariableExpressionExecutor) left).getPosition();
                leftVariable = (Float) complexEvent.getAttribute(((VariableExpressionExecutor) left).getPosition());
                beforeWindowIndexLeft = leftPosition[SiddhiConstants.STREAM_ATTRIBUTE_INDEX_IN_TYPE];
                Test2.methodVisitor.visitVarInsn(ALOAD, 2);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/event/stream/StreamEvent");
                Test2.methodVisitor.visitVarInsn(ASTORE, 7);
                Test2.methodVisitor.visitVarInsn(ALOAD, 7);
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/event/stream/StreamEvent", "getBeforeWindowData", "()[Ljava/lang/Object;", false);
                Test2.methodVisitor.visitVarInsn(ASTORE, 8);
                Test2.methodVisitor.visitVarInsn(ALOAD, 8);
                Test2.methodVisitor.visitIntInsn(BIPUSH, beforeWindowIndexLeft);
                Test2.methodVisitor.visitInsn(AALOAD);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                Test2.methodVisitor.visitVarInsn(FSTORE, 5);
            }else if(left instanceof ConstantExpressionExecutor){
                leftVariable = (Float) ((ConstantExpressionExecutor) left).getValue();
                Test2.methodVisitor.visitLdcInsn(new Float(leftVariable));
                Test2.methodVisitor.visitVarInsn(FSTORE, 5);
            }
            if(right instanceof VariableExpressionExecutor){
                rightPosition = ((VariableExpressionExecutor) right).getPosition();
                rightVariable = (Double) complexEvent.getAttribute(((VariableExpressionExecutor) right).getPosition());
                beforeWindowIndexRight = rightPosition[SiddhiConstants.STREAM_ATTRIBUTE_INDEX_IN_TYPE];
                Test2.methodVisitor.visitVarInsn(ALOAD, 2);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/event/stream/StreamEvent");
                Test2.methodVisitor.visitVarInsn(ASTORE, 7);
                Test2.methodVisitor.visitVarInsn(ALOAD, 7);
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/event/stream/StreamEvent", "getBeforeWindowData", "()[Ljava/lang/Object;", false);
                Test2.methodVisitor.visitVarInsn(ASTORE, 9);
                Test2.methodVisitor.visitVarInsn(ALOAD, 9);
                Test2.methodVisitor.visitIntInsn(BIPUSH, beforeWindowIndexRight);
                Test2.methodVisitor.visitInsn(AALOAD);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                Test2.methodVisitor.visitVarInsn(DSTORE, 6);
            }else if(right instanceof  ConstantExpressionExecutor){
                rightVariable = (Double) ((ConstantExpressionExecutor) right).getValue();
                Test2.methodVisitor.visitLdcInsn(new Double(rightVariable));
                Test2.methodVisitor.visitVarInsn(DSTORE, 6);
            }
            Test2.methodVisitor.visitVarInsn(FLOAD, 5);
            Test2.methodVisitor.visitInsn(F2D);
            Test2.methodVisitor.visitVarInsn(DLOAD, 6);
            Test2.methodVisitor.visitInsn(DCMPL);
            Label l0 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFLE, l0);
            Test2.methodVisitor.visitInsn(ICONST_1);
            Label l1 = new Label();
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l0);
            Test2.methodVisitor.visitInsn(ICONST_0);
            Test2.methodVisitor.visitLabel(l1);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            return leftVariable > rightVariable;

        }else if(conditionExecutor instanceof LessThanCompareConditionExpressionExecutorFloatDouble){
            //System.out.println("<");
            ExpressionExecutor left = ((LessThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getLeftExpressionExecutor();
            ExpressionExecutor right = ((LessThanCompareConditionExpressionExecutorFloatDouble) conditionExecutor).getRightExpressionExecutor();
            Float leftVariable = null;
            Double rightVariable = null;
            int[] leftPosition = null;
            int[] rightPosition = null;
            int beforeWindowIndexLeft = 0;
            int beforeWindowIndexRight = 0;

            if(left instanceof VariableExpressionExecutor){
                leftPosition = ((VariableExpressionExecutor) left).getPosition();
                leftVariable = (Float) complexEvent.getAttribute(((VariableExpressionExecutor) left).getPosition());
                beforeWindowIndexLeft = leftPosition[SiddhiConstants.STREAM_ATTRIBUTE_INDEX_IN_TYPE];
                Test2.methodVisitor.visitVarInsn(ALOAD, 2);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/event/stream/StreamEvent");
                Test2.methodVisitor.visitVarInsn(ASTORE, 7);
                Test2.methodVisitor.visitVarInsn(ALOAD, 7);
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/event/stream/StreamEvent", "getBeforeWindowData", "()[Ljava/lang/Object;", false);
                Test2.methodVisitor.visitVarInsn(ASTORE, 8);
                Test2.methodVisitor.visitVarInsn(ALOAD, 8);
                Test2.methodVisitor.visitIntInsn(BIPUSH, beforeWindowIndexLeft);
                Test2.methodVisitor.visitInsn(AALOAD);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Float");
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                Test2.methodVisitor.visitVarInsn(FSTORE, 5);
            }else if(left instanceof ConstantExpressionExecutor){
                leftVariable = (Float) ((ConstantExpressionExecutor) left).getValue();
                Test2.methodVisitor.visitLdcInsn(new Float(leftVariable));
                Test2.methodVisitor.visitVarInsn(FSTORE, 5);
            }
            if(right instanceof VariableExpressionExecutor){
                rightPosition = ((VariableExpressionExecutor) right).getPosition();
                rightVariable = (Double) complexEvent.getAttribute(((VariableExpressionExecutor) right).getPosition());
                beforeWindowIndexRight = rightPosition[SiddhiConstants.STREAM_ATTRIBUTE_INDEX_IN_TYPE];
                Test2.methodVisitor.visitVarInsn(ALOAD, 2);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "org/wso2/siddhi/core/event/stream/StreamEvent");
                Test2.methodVisitor.visitVarInsn(ASTORE, 7);
                Test2.methodVisitor.visitVarInsn(ALOAD, 7);
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/wso2/siddhi/core/event/stream/StreamEvent", "getBeforeWindowData", "()[Ljava/lang/Object;", false);
                Test2.methodVisitor.visitVarInsn(ASTORE, 9);
                Test2.methodVisitor.visitVarInsn(ALOAD, 9);
                Test2.methodVisitor.visitIntInsn(BIPUSH, beforeWindowIndexRight);
                Test2.methodVisitor.visitInsn(AALOAD);
                Test2.methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/Double");
                Test2.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                Test2.methodVisitor.visitVarInsn(DSTORE, 6);

            }else if(right instanceof  ConstantExpressionExecutor){
                rightVariable = (Double) ((ConstantExpressionExecutor) right).getValue();
                Test2.methodVisitor.visitLdcInsn(new Double(rightVariable));
                Test2.methodVisitor.visitVarInsn(DSTORE, 6);
            }
            Test2.methodVisitor.visitVarInsn(FLOAD, 5);
            Test2.methodVisitor.visitInsn(F2D);
            Test2.methodVisitor.visitVarInsn(DLOAD, 6);
            Test2.methodVisitor.visitInsn(DCMPG);
            Label l0 = new Label();
            Test2.methodVisitor.visitJumpInsn(IFGE, l0);
            Test2.methodVisitor.visitInsn(ICONST_1);
            Label l1 = new Label();
            Test2.methodVisitor.visitJumpInsn(GOTO, l1);
            Test2.methodVisitor.visitLabel(l0);
            Test2.methodVisitor.visitInsn(ICONST_0);
            Test2.methodVisitor.visitLabel(l1);
            if(status == 2){
                Test2.methodVisitor.visitVarInsn(ISTORE, 4);
            }else{
                Test2.methodVisitor.visitVarInsn(ISTORE, 3);
            }
            return leftVariable < rightVariable;

        }else{
            return true;
        }
    }
}
