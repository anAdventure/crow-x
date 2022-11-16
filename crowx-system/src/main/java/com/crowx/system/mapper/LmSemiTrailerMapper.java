package com.crowx.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowx.system.domain.LmSemiTrailer;

import java.util.List;

/**
 * 半挂Mapper接口
 * 
 * @author xiezihao
 * @date 2022-10-15
 */
public interface LmSemiTrailerMapper  extends BaseMapper<LmSemiTrailer>
{
    /**
     * 查询半挂
     * 
     * @param id 半挂主键
     * @return 半挂
     */
    public LmSemiTrailer selectLmSemiTrailerById(Long id);

    /**
     * 查询半挂列表
     * 
     * @param lmSemiTrailer 半挂
     * @return 半挂集合
     */
    public List<LmSemiTrailer> selectLmSemiTrailerList(LmSemiTrailer lmSemiTrailer);

    /**
     * 新增半挂
     * 
     * @param lmSemiTrailer 半挂
     * @return 结果
     */
    public int insertLmSemiTrailer(LmSemiTrailer lmSemiTrailer);

    /**
     * 修改半挂
     * 
     * @param lmSemiTrailer 半挂
     * @return 结果
     */
    public int updateLmSemiTrailer(LmSemiTrailer lmSemiTrailer);

    /**
     * 删除半挂
     * 
     * @param id 半挂主键
     * @return 结果
     */
    public int deleteLmSemiTrailerById(Long id);

    /**
     * 批量删除半挂
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLmSemiTrailerByIds(Long[] ids);
}
