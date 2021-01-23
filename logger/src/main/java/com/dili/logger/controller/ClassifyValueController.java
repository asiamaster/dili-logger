package com.dili.logger.controller;

import cn.hutool.json.JSONUtil;
import com.dili.logger.domain.ClassifyValue;
import com.dili.logger.service.ClassifyValueService;
import com.dili.ss.domain.BaseOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;
import java.util.Optional;

/**
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/1/23 15:04
 */
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/classifyValue")
public class ClassifyValueController {

    private final ClassifyValueService classifyValueService;

    /**
     * 跳转到类型值管理页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "classifyValue/list";
    }

    /**
     * 查询类型值列表信息
     * @param classifyValue
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listPage(@RequestBody ClassifyValue classifyValue) throws Exception {
        return classifyValueService.listEasyuiPageByExample(classifyValue, true).toString();
    }

    /**
     * 进入类型值预编辑页面
     * @param id 数据ID
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/preSave.action", method = {RequestMethod.GET})
    public String preSave(Long id, ModelMap modelMap) {
        if (Objects.nonNull(id)) {
            ClassifyValue classifyValue = classifyValueService.get(id);
            modelMap.put("classifyValue", JSONUtil.parseObj(classifyValue));
        } else {
            modelMap.put("classifyValue", "{}");
        }
        return "classifyValue/edit";
    }

    /**
     * 保存类型值信息
     * @param classifyValue
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput save(@Validated @RequestBody ClassifyValue classifyValue, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            Optional<String> s = classifyValueService.saveData(classifyValue);
            if (s.isPresent()) {
                return BaseOutput.failure(s.get());
            }
            return BaseOutput.success("保存成功");
        } catch (Exception e) {
            log.error(String.format("保存类型值信息:%s 异常:%s", JSONUtil.toJsonStr(classifyValue), e.getMessage()), e);
            return BaseOutput.failure("系统异常");
        }
    }
}
