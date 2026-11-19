package com.pangpanglink.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangpanglink.shortlink.admin.dao.entity.GroupDO;
import com.pangpanglink.shortlink.admin.dao.mapper.GroupMapper;
import com.pangpanglink.shortlink.admin.service.RecycleBinAdapterService;
import com.pangpanglink.shortlink.common.biz.user.UserContext;
import com.pangpanglink.shortlink.common.convention.exception.ServiceException;
import com.pangpanglink.shortlink.common.convention.result.Result;
import com.pangpanglink.shortlink.common.convention.result.Results;
import com.pangpanglink.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.pangpanglink.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.pangpanglink.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回收站查询适配接口实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinAdapterServiceImpl implements RecycleBinAdapterService {

    private final RecycleBinService projectRecycleBinService;
    private final GroupMapper groupMapper;

    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ServiceException("用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return Results.success(projectRecycleBinService.pageShortLink(requestParam));
    }
}
