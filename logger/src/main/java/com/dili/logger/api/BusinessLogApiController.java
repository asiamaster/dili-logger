package com.dili.logger.api;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.dili.logger.domain.BusinessLog;
import com.dili.logger.sdk.domain.input.BusinessLogQueryInput;
import com.dili.logger.service.BusinessLogService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.mvc.util.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/11 14:19
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/businessLog")
public class BusinessLogApiController {

    private final BusinessLogService businessLogService;

    /**
     * 保存业务日志数据
     *
     * @param condition
     * @return
     */
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/save",method = {RequestMethod.POST})
    public BaseOutput save(@RequestBody BusinessLog condition, HttpServletRequest httpServletRequest) {
        if (StrUtil.isBlank(condition.getRemoteIp())) {
            condition.setRemoteIp(RequestUtils.getIpAddress(httpServletRequest));
        }
        if (StrUtil.isBlank(condition.getServerIp())) {
            condition.setServerIp(httpServletRequest.getLocalAddr());
        }
        businessLogService.save(condition);
        return BaseOutput.success();
    }

    /**
     * 批量保存业务日志信息
     * @param businessLogList
     * @return
     */
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/batchSave",method = {RequestMethod.POST})
    public BaseOutput batchSave(@RequestBody List<BusinessLog> businessLogList,HttpServletRequest httpServletRequest) {
        String remoteIp = RequestUtils.getIpAddress(httpServletRequest);
        String serverIp = httpServletRequest.getLocalAddr();
        if (CollectionUtil.isNotEmpty(businessLogList)) {
            businessLogList.forEach(l -> {
                if (StrUtil.isBlank(l.getRemoteIp())) {
                    l.setRemoteIp(remoteIp);
                }
                if (StrUtil.isBlank(l.getServerIp())) {
                    l.setServerIp(serverIp);
                }
            });
        }
        businessLogService.batchSave(businessLogList);
        return BaseOutput.success();
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage", method = {RequestMethod.POST})
    public PageOutput listPage(@RequestBody(required = false) BusinessLogQueryInput condition) {
        try {
            return businessLogService.searchPage(condition);
        }catch (Throwable throwable){
            log.error(String.format("分页查询业务日志异常[%s]",throwable.getMessage()),throwable);
            return PageOutput.failure("数据查询异常");
        }

    }

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public BaseOutput list(@RequestBody(required = false) BusinessLogQueryInput condition) {
        try {
            return businessLogService.list(condition);
        }catch (Throwable throwable){
            log.error(String.format("查询业务日志异常[%s]",throwable.getMessage()),throwable);
            return PageOutput.failure("数据查询异常");
        }
    }


    /**
     * 删除对应的所有数据
     *
     * @return
     */
    @RequestMapping(value = "/deleteAll", method = {RequestMethod.POST})
    public BaseOutput deleteAll(@RequestParam("u_name") String userName, @RequestParam("u_pass") String password) {
        if (Objects.isNull(userName) || Objects.isNull(password)) {
            return BaseOutput.failure("禁止访问").setCode(ResultCode.FORBIDDEN);
        }
        if ("eb47ff362ab6826c1ff83e64374a12d2".equalsIgnoreCase(MD5.create().digestHex(userName)) && "468fabd568997e46672921afaa5417b2".equalsIgnoreCase(MD5.create().digestHex(password))) {
            businessLogService.deleteAll();
            return BaseOutput.success("删除成功");
        }
        return BaseOutput.failure("无权访问").setCode(ResultCode.UNAUTHORIZED);
    }
}
