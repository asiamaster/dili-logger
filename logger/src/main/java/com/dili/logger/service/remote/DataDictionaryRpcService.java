package com.dili.logger.service.remote;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <B>Cross跨域请求拦截器</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/24 15:07
 */
@Service
public class DataDictionaryRpcService {

    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    /**
     * 根据数据字典code获取对应的值列表
     * @param ddCode
     * @return
     */
    public List<DataDictionaryValue> listDataDictionaryValueByDdCode(String ddCode){
        if (StrUtil.isNotBlank(ddCode)) {
            DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
            dataDictionaryValue.setDdCode(ddCode);
            BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
            if (output.isSuccess()) {
                return output.getData();
            }
        }
        return Collections.emptyList();
    }

    /**
     * 根据数据字典码及值编码获取数据信息
     * @param ddCode 数据字典码
     * @param code   值编码
     * @return
     */
    public Optional<DataDictionaryValue> getByDdCodeAndCode(String ddCode, String code) {
        if (StrUtil.isNotBlank(ddCode) && StrUtil.isNotBlank(code)) {
            DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
            dataDictionaryValue.setDdCode(ddCode);
            dataDictionaryValue.setCode(code);
            BaseOutput<List<DataDictionaryValue>> output = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
            if (output.isSuccess() && CollectionUtil.isNotEmpty(output.getData())) {
                return Optional.ofNullable(output.getData().get(0));
            }
        }
        return Optional.empty();
    }

    /**
     * 获取操作类型并转换的map对象
     * @return
     */
    public Map<String, DataDictionaryValue> getOperationTypeMap() {
        List<DataDictionaryValue> operationTypeList = listDataDictionaryValueByDdCode("operation_type");
        if (CollectionUtil.isNotEmpty(operationTypeList)) {
            return operationTypeList.stream().collect(Collectors.toMap(DataDictionaryValue::getCode, Function.identity()));
        }
        return Collections.emptyMap();
    }

    /**
     * 获取异常类型并转换的map对象
     * @return
     */
    public Map<String, DataDictionaryValue> getExceptionTypeMap() {
        List<DataDictionaryValue> operationTypeList = listDataDictionaryValueByDdCode("exception_type");
        if (CollectionUtil.isNotEmpty(operationTypeList)) {
            return operationTypeList.stream().collect(Collectors.toMap(DataDictionaryValue::getCode, Function.identity()));
        }
        return Collections.emptyMap();
    }
}
