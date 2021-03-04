package com.dili.logger.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 日志系统工具类
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/3/4 16:27
 */
public class LogAppUtil {

    /**
     * 根据传入的IP地址字符串，只获取第一个地址
     * @param ip
     * @return
     */
    public static String cutFirstIp(String ip) {
        if (StrUtil.isNotBlank(ip)) {
            return ip.split(",")[0];
        }
        return null;
    }
}
