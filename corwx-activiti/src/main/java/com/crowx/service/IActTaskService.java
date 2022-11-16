package com.crowx.service;

import com.crowx.common.core.page.PageDomain;
import com.crowx.domain.dto.ActTaskDTO;
import com.crowx.domain.dto.ActWorkflowFormDataDTO;
import com.crowx.domain.dto.AddTaskDTO;
import com.github.pagehelper.Page;
import org.activiti.engine.runtime.ProcessInstance;

import java.text.ParseException;
import java.util.List;

public interface IActTaskService {
    public Page<ActTaskDTO> selectProcessDefinitionList(PageDomain pageDomain);
    public List<String>formDataShow(String taskID);
    public int formDataSave(String taskID, List<ActWorkflowFormDataDTO> awfs) throws ParseException;

    org.activiti.api.process.model.ProcessInstance addTasks(AddTaskDTO addTaskDTO);
}
