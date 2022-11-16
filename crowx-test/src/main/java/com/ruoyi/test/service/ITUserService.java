package com.ruoyi.test.service;

import com.ruoyi.test.domain.TUser;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-03-19
 */
public interface ITUserService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param username 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public TUser selectTUserByUsername(String username);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param tUser 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TUser> selectTUserList(TUser tUser);

    /**
     * 新增【请填写功能名称】
     * 
     * @param tUser 【请填写功能名称】
     * @return 结果
     */
    public int insertTUser(TUser tUser);

    /**
     * 修改【请填写功能名称】
     * 
     * @param tUser 【请填写功能名称】
     * @return 结果
     */
    public int updateTUser(TUser tUser);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param usernames 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteTUserByUsernames(String[] usernames);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param username 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteTUserByUsername(String username);
}
