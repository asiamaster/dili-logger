package com.dili.logger.sdk.rpc;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/10 15:16
 */
@FeignClient(name = "dili-logger")
public interface ExceptionLogRpc {
}
