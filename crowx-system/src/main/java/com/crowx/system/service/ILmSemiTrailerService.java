package com.crowx.system.service;

import java.util.List;
import com.crowx.system.domain.LmSemiTrailer;

/**
 * 半挂Service接口
 * 
 * @author xiezihao
 * @date 2022-10-15
 */
public interface ILmSemiTrailerService 
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
     * 批量删除半挂
     * 
     * @param ids 需要删除的半挂主键集合
     * @return 结果
     */
    public int deleteLmSemiTrailerByIds(Long[] ids);

    /**
     * 删除半挂信息
     * 
     * @param id 半挂主键
     * @return 结果
     */
    public int deleteLmSemiTrailerById(Long id);
}
