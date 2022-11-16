package com.crowx.controller;


import com.crowx.common.core.domain.AjaxResult;
import com.crowx.domain.dto.ActivitiHighLineDTO;
import com.crowx.service.IActivitiHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/activitiHistory")
public class ActivitiHistoryController {

    @Autowired
    private IActivitiHistoryService activitiHistoryService;

    //流程图高亮
    @GetMapping("/gethighLine")
    public AjaxResult gethighLine(@RequestParam("instanceId") String instanceId) {

        ActivitiHighLineDTO activitiHighLineDTO = activitiHistoryService.gethighLine(instanceId);
        return AjaxResult.success(activitiHighLineDTO);


    }


}
