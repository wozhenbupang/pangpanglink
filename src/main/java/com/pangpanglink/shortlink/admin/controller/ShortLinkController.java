package com.pangpanglink.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pangpanglink.shortlink.admin.dto.resp.ShortLinkExcelExportDTO;
import com.pangpanglink.shortlink.admin.toolkit.EasyExcelWebUtil;
import com.pangpanglink.shortlink.common.convention.result.Result;
import com.pangpanglink.shortlink.common.convention.result.Results;
import com.pangpanglink.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.pangpanglink.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.pangpanglink.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.pangpanglink.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.pangpanglink.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.pangpanglink.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.pangpanglink.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.pangpanglink.shortlink.project.handler.CustomBlockHandler;
import com.pangpanglink.shortlink.project.service.ShortLinkService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 短链接后管控制层
 */
@RestController(value = "shortLinkControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createShortLinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 批量创建短链接
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        ShortLinkBatchCreateRespDTO result = shortLinkService.batchCreateShortLink(requestParam);
        List<ShortLinkExcelExportDTO> exportList = BeanUtil.copyToList(result.getBaseLinkInfos(), ShortLinkExcelExportDTO.class);
        EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkExcelExportDTO.class, exportList);
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }
}
