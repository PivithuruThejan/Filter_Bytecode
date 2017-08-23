package org.wso2.siddhi.core.query.processor.filter;

import org.wso2.siddhi.core.event.ComplexEvent;
import org.wso2.siddhi.core.event.stream.StreamEvent;

public class Test {
    public boolean execute(ComplexEvent complexEvent){
        StreamEvent streamEvent = (StreamEvent) complexEvent;
        Object[] eventData  = streamEvent.getBeforeWindowData();
        double t = (double) eventData[0];
        float testAverage = (float)eventData[0];
        float odiAverage = (float)eventData[1];
        float odiStrikeRate = (float)eventData[2];
        float t20Average = (float)eventData[3];
        float t20StrikeRate = (float)eventData[4];
        return testAverage>45.0&(odiAverage>40.0 |odiStrikeRate >100.0) & !(t20Average <10.0 | t20StrikeRate >150.0);
    }
}
