package com.crowx.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 监听器案例
 */
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        if(delegateTask.getName().equals("部门领导审批")){
            String eventName = delegateTask.getEventName();
            System.out.println(eventName);
            if(eventName.equals("create")){
                delegateTask.setAssignee("jack");
            }
        }
    }
}
