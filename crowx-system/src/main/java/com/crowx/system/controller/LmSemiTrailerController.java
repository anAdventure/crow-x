package com.crowx.system.controller;

import java.util.List;

import com.crowx.system.domain.LmSemiTrailer;
import com.crowx.system.service.ILmSemiTrailerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crowx.common.annotation.Log;
import com.crowx.common.core.controller.BaseController;
import com.crowx.common.core.domain.AjaxResult;
import com.crowx.common.enums.BusinessType;
import com.crowx.common.utils.poi.ExcelUtil;
import com.crowx.common.core.page.TableDataInfo;

/**
 * 半挂Controller
 * 
 * @author xiezihao
 * @date 2022-10-15
 */
@RestController
@RequestMapping("/system/trailer")
public class LmSemiTrailerController extends BaseController
{
    @Autowired
    private ILmSemiTrailerService lmSemiTrailerService;

    /**
     * 查询半挂列表
     */
    @PreAuthorize("@ss.hasPermi('system:trailer:list')")
    @GetMapping("/list")
    public TableDataInfo list(LmSemiTrailer lmSemiTrailer)
    {
        startPage();
        List<LmSemiTrailer> list = lmSemiTrailerService.selectLmSemiTrailerList(lmSemiTrailer);
        return getDataTable(list);
    }

    /**
     * 导出半挂列表
     */
    @PreAuthorize("@ss.hasPermi('system:trailer:export')")
    @Log(title = "半挂", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(LmSemiTrailer lmSemiTrailer)
    {
        List<LmSemiTrailer> list = lmSemiTrailerService.selectLmSemiTrailerList(lmSemiTrailer);
        ExcelUtil<LmSemiTrailer> util = new ExcelUtil<LmSemiTrailer>(LmSemiTrailer.class);
        return util.exportExcel(list, "半挂数据");
    }

    /**
     * 获取半挂详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:trailer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(lmSemiTrailerService.selectLmSemiTrailerById(id));
    }

    /**
     * 新增半挂
     */
    @PreAuthorize("@ss.hasPermi('system:trailer:add')")
    @Log(title = "半挂", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LmSemiTrailer lmSemiTrailer)
    {
        return toAjax(lmSemiTrailerService.insertLmSemiTrailer(lmSemiTrailer));
    }

    /**
     * 修改半挂
     */
    @PreAuthorize("@ss.hasPermi('system:trailer:edit')")
    @Log(title = "半挂", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LmSemiTrailer lmSemiTrailer)
    {
        return toAjax(lmSemiTrailerService.updateLmSemiTrailer(lmSemiTrailer));
    }

    /**
     * 删除半挂
     */
    @PreAuthorize("@ss.hasPermi('system:trailer:remove')")
    @Log(title = "半挂", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lmSemiTrailerService.deleteLmSemiTrailerByIds(ids));
    }
}
