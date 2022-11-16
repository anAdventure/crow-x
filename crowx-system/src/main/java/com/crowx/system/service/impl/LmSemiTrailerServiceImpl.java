package com.crowx.system.service.impl;

import java.util.List;
import com.crowx.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crowx.system.mapper.LmSemiTrailerMapper;
import com.crowx.system.domain.LmSemiTrailer;
import com.crowx.system.service.ILmSemiTrailerService;

/**
 * 半挂Service业务层处理
 * 
 * @author xiezihao
 * @date 2022-10-15
 */
@Service
public class LmSemiTrailerServiceImpl implements ILmSemiTrailerService 
{
    @Autowired
    private LmSemiTrailerMapper lmSemiTrailerMapper;

    /**
     * 查询半挂
     * 
     * @param id 半挂主键
     * @return 半挂
     */
    @Override
    public LmSemiTrailer selectLmSemiTrailerById(Long id)
    {
        return lmSemiTrailerMapper.selectLmSemiTrailerById(id);
    }

    /**
     * 查询半挂列表
     * 
     * @param lmSemiTrailer 半挂
     * @return 半挂
     */
    @Override
    public List<LmSemiTrailer> selectLmSemiTrailerList(LmSemiTrailer lmSemiTrailer)
    {
        return lmSemiTrailerMapper.selectLmSemiTrailerList(lmSemiTrailer);
    }

    /**
     * 新增半挂
     * 
     * @param lmSemiTrailer 半挂
     * @return 结果
     */
    @Override
    public int insertLmSemiTrailer(LmSemiTrailer lmSemiTrailer)
    {
        lmSemiTrailer.setCreateTime(DateUtils.getNowDate());
        return lmSemiTrailerMapper.insert(lmSemiTrailer);
    }

    /**
     * 修改半挂
     * 
     * @param lmSemiTrailer 半挂
     * @return 结果
     */
    @Override
    public int updateLmSemiTrailer(LmSemiTrailer lmSemiTrailer)
    {
        lmSemiTrailer.setUpdateTime(DateUtils.getNowDate());
        return lmSemiTrailerMapper.updateById(lmSemiTrailer);
    }

    /**
     * 批量删除半挂
     * 
     * @param ids 需要删除的半挂主键
     * @return 结果
     */
    @Override
    public int deleteLmSemiTrailerByIds(Long[] ids)
    {
        return lmSemiTrailerMapper.deleteLmSemiTrailerByIds(ids);
    }

    /**
     * 删除半挂信息
     * 
     * @param id 半挂主键
     * @return 结果
     */
    @Override
    public int deleteLmSemiTrailerById(Long id)
    {
        return lmSemiTrailerMapper.deleteLmSemiTrailerById(id);
    }
}
