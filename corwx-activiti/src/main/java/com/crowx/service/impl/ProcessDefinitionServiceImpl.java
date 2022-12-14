package com.crowx.service.impl;

import com.crowx.common.config.RuoYiConfig;
import com.crowx.common.core.page.PageDomain;
import com.crowx.common.utils.StringUtils;
import com.crowx.common.utils.file.FileUploadUtils;
import com.crowx.domain.dto.DefinitionIdDTO;
import com.crowx.domain.dto.ProcessDefinitionDTO;
import com.crowx.domain.vo.ActReDeploymentVO;
import com.crowx.mapper.ActReDeploymentMapper;
import com.crowx.service.IProcessDefinitionService;
import com.github.pagehelper.Page;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;


@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActReDeploymentMapper actReDeploymentMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public Page<ProcessDefinitionDTO> selectProcessDefinitionList(ProcessDefinitionDTO processDefinition, PageDomain pageDomain) {
        Page<ProcessDefinitionDTO> list = new Page<>();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionId().orderByProcessDefinitionVersion().desc();
        if (StringUtils.isNotBlank(processDefinition.getName())) {
            processDefinitionQuery.processDefinitionNameLike("%" + processDefinition.getName() + "%");
        }
        if (StringUtils.isNotBlank(processDefinition.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike("%" + processDefinition.getKey() + "%");
        }
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize());
        long count = processDefinitionQuery.count();
        list.setTotal(count);
        if (count!=0) {
            Set<String> ids = processDefinitions.parallelStream().map(pdl -> pdl.getDeploymentId()).collect(Collectors.toSet());
            List<ActReDeploymentVO> actReDeploymentVOS = actReDeploymentMapper.selectActReDeploymentByIds(ids);
            List<ProcessDefinitionDTO> processDefinitionDTOS = processDefinitions.stream()
                    .map(pd -> new ProcessDefinitionDTO((ProcessDefinitionEntityImpl) pd, actReDeploymentVOS.parallelStream().filter(ard -> pd.getDeploymentId().equals(ard.getId())).findAny().orElse(new ActReDeploymentVO())))
                    .collect(Collectors.toList());
            list.addAll(processDefinitionDTOS);
        }
        return list;
    }

    @Override
    public DefinitionIdDTO getDefinitionsByInstanceId(String instanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String deploymentId = processInstance.getDeploymentId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        return new DefinitionIdDTO(processDefinition);
    }

    @Override
    public int deleteProcessDefinitionById(String id) {
        repositoryService.deleteDeployment(id, false);
        return 1;
    }

    @Override
    public void uploadStreamAndDeployment(MultipartFile file) throws IOException {

        // ????????????????????????
        String fileName = file.getOriginalFilename();
        // ????????????????????????????????????
        InputStream fileInputStream = file.getInputStream();
        // ??????????????????
        String extension = FilenameUtils.getExtension(fileName);

        if (extension.equals("zip")) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            repositoryService.createDeployment()//???????????????
                    .addZipInputStream(zip)
                    .deploy();
        } else {
            repositoryService.createDeployment()//???????????????
                    .addInputStream(fileName, fileInputStream)

                    .deploy();
        }
    }

    @Override
    public void suspendOrActiveApply(String id, Integer suspendState) {
        if (1==suspendState) {
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            // ?????????????????????????????????????????????????????????????????????????????????
            // ???????????????act_re_procdef ??? SUSPENSION_STATE_ ??? 2

            //????????????????????????????????????????????????????????????????????????????????????
            //???????????????????????????????????????id
            //????????????????????????????????????????????????????????????????????????
            //?????????????????????????????????????????????????????????????????????????????????????????????
            repositoryService.suspendProcessDefinitionById(id);
        } else if (2==suspendState) {
            repositoryService.activateProcessDefinitionById(id);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
       return FileUploadUtils.upload(RuoYiConfig.getUploadPath()+"/processDefinition" , multipartFile);
    }

    @Override
    public void addDeploymentByString(String stringBPMN) {
        repositoryService.createDeployment()
                .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                .deploy();
    }

    @Override
    public void getProcessDefineXML(HttpServletResponse response, String deploymentId, String resourceName) throws IOException {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        int count = inputStream.available();
        byte[] bytes = new byte[count];
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();
        while (inputStream.read(bytes) != -1) {
            outputStream.write(bytes);
        }
        inputStream.close();
    }
}
