package com.pangpanglink.shortlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pangpanglink.shortlink.common.convention.result.Result;
import com.pangpanglink.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.pangpanglink.shortlink.project.dto.resp.ShortLinkPageRespDTO;

/**
 * 回收站查询适配接口层
 */
public interface RecycleBinAdapterService {

    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return 返回参数包装
     */
    Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
