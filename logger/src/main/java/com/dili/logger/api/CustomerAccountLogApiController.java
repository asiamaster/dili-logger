package com.dili.logger.api;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.dili.logger.domain.CustomerAccountLog;
import com.dili.logger.sdk.domain.input.CustomerAccountLogQueryInput;
import com.dili.logger.service.CustomerAccountLogService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.mvc.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/17 18:03
 */
@RestController
@RequestMapping("/api/customerAccountLog")
public class CustomerAccountLogApiController {

    @Autowired
    private CustomerAccountLogService customerAccountLogService;

    /**
     * 保存业务日志数据
     *
     * @param condition
     * @return
     */
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/save",method = {RequestMethod.POST})
    public BaseOutput save(@RequestBody CustomerAccountLog condition, HttpServletRequest httpServletRequest) {
        if (StrUtil.isBlank(condition.getRemoteIp())) {
            condition.setRemoteIp(RequestUtils.getIpAddress(httpServletRequest));
        }
        if (StrUtil.isBlank(condition.getServerIp())) {
            condition.setServerIp(httpServletRequest.getLocalAddr());
        }
        customerAccountLogService.save(condition);
        return BaseOutput.success();
    }

    /**
     * 批量保存业务日志信息
     * @param customerAccountLogList
     * @return
     */
    @CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/batchSave",method = {RequestMethod.POST})
    public BaseOutput batchSave(@RequestBody List<CustomerAccountLog> customerAccountLogList, HttpServletRequest httpServletRequest) {
        String remoteIp = RequestUtils.getIpAddress(httpServletRequest);
        String serverIp = httpServletRequest.getLocalAddr();
        if (CollectionUtil.isNotEmpty(customerAccountLogList)) {
            customerAccountLogList.forEach(l -> {
                if (StrUtil.isBlank(l.getRemoteIp())) {
                    l.setRemoteIp(remoteIp);
                }
                if (StrUtil.isBlank(l.getServerIp())) {
                    l.setServerIp(serverIp);
                }
            });
        }
        customerAccountLogService.batchSave(customerAccountLogList);
        return BaseOutput.success();
    }

    /**
     * 分页查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/listPage", method = {RequestMethod.POST})
    public PageOutput listPage(@RequestBody(required = false) CustomerAccountLogQueryInput condition) {
        return customerAccountLogService.searchPage(condition);
    }

    /**
     * 查询日志数据
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public BaseOutput list(@RequestBody(required = false) CustomerAccountLogQueryInput condition) {
        return customerAccountLogService.list(condition);
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
            customerAccountLogService.deleteAll();
            return BaseOutput.success("删除成功");
        }
        return BaseOutput.failure("无权访问").setCode(ResultCode.UNAUTHORIZED);
    }
}
