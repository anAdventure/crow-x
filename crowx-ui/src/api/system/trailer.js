import request from '@/utils/request'

// 查询半挂列表
export function listTrailer(query) {
  return request({
    url: '/system/trailer/list',
    method: 'get',
    params: query
  })
}

// 查询半挂详细
export function getTrailer(id) {
  return request({
    url: '/system/trailer/' + id,
    method: 'get'
  })
}

// 新增半挂
export function addTrailer(data) {
  return request({
    url: '/system/trailer',
    method: 'post',
    data: data
  })
}

// 修改半挂
export function updateTrailer(data) {
  return request({
    url: '/system/trailer',
    method: 'put',
    data: data
  })
}

// 删除半挂
export function delTrailer(id) {
  return request({
    url: '/system/trailer/' + id,
    method: 'delete'
  })
}

// 导出半挂
export function exportTrailer(query) {
  return request({
    url: '/system/trailer/export',
    method: 'get',
    params: query
  })
}