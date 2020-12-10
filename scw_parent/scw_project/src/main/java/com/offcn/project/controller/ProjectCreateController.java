package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.po.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/project")
@Api(tags = "项目基本功能-创建、保存项目")
@Slf4j
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("项目发起第1步-阅读同意协议")
    @GetMapping("/init")
    public AppResponse<String> init(BaseVo vo) {
        String accessToken = vo.getAccessToken();
        String memberIdStr = stringRedisTemplate.opsForValue().get(accessToken);

        if(!StringUtils.isEmpty(memberIdStr)) {
            String proToken = projectCreateService.initCreateProject(Integer.parseInt(memberIdStr));
            return AppResponse.ok(proToken);
        }
        else {
            AppResponse<String> fail = AppResponse.fail("初始化项目失败");
            fail.setMsg("登录信息失效，请登录");
            return fail;
        }
    }


    @ApiOperation("项目发起第2步-保存项目的基本信息")
    @PostMapping("/savebaseInfo")
    public AppResponse<String> savebaseInfo(ProjectBaseInfoVo vo) {
        String proToken = vo.getProjectToken();

        String proStr = stringRedisTemplate.opsForValue().get(proToken);

        if(!StringUtils.isEmpty(proStr)) {

            // 1. 先将从redis获取回来的 项目信息 转为 对象
            ProjectRedisStorageVo proRedisVo = JSON.parseObject(proStr, ProjectRedisStorageVo.class);

            // 2. 将页面传递过来的vo属性 合并 到 proRedisVo 中
            BeanUtils.copyProperties(vo, proRedisVo);

            // 3. 将合并后的proRedisVo重新保存到redis中
            stringRedisTemplate.opsForValue().set(proToken, JSON.toJSONString(proRedisVo), 7, TimeUnit.DAYS);

            return AppResponse.ok(proToken);
        }
        else {
            AppResponse<String> fail = AppResponse.fail("保存项目基本信息失败");
            fail.setMsg("请返回上一步完成阅读协议");
            return fail;
        }
    }


    @ApiOperation("项目发起第3步-保存项目回报信息")
    @PostMapping("/savereturn")
    public AppResponse<String> savereturn(@RequestBody List<ProjectReturnVo> proList) {
        // 一个项目，可以规定多种回报手段：
        // 如每笔1000块，固定投30，采取货币回报的方式
        // 如每笔2000块，固定投40，采取货币回报的方式

        // 通过传递过来的多个项目回报，任意取其中一个得到 项目token
        String proToken = proList.get(0).getProjectToken();

        String proStr = stringRedisTemplate.opsForValue().get(proToken);

        if(!StringUtils.isEmpty(proStr)) {

            ProjectRedisStorageVo proRedisVo = JSON.parseObject(proStr, ProjectRedisStorageVo.class);

            List<TReturn> list = new ArrayList();
            for (ProjectReturnVo proReturnVo : proList) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(proReturnVo, tReturn);
                list.add(tReturn);
            }
            proRedisVo.setProjectReturns(list);

            stringRedisTemplate.opsForValue().set(proToken, JSON.toJSONString(proRedisVo), 7, TimeUnit.DAYS);
            return AppResponse.ok(proToken);
        }
        else {
            AppResponse<String> fail = AppResponse.fail("保存项目回报信息失败");
            fail.setMsg("请返回上一步填写项目的基本信息");
            return fail;
        }
    }


    @ApiOperation("项目发起第4步-保存项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectToken", value = "项目标识", required = true),
            @ApiImplicitParam(name = "ops", value = "用户操作类型 0-即将开始 1-众筹中", required = true)})
    @PostMapping("/submit")
    public AppResponse<Object> submit(String projectToken, String ops) {

        projectToken = stringRedisTemplate.opsForValue().get(projectToken);
        ProjectRedisStorageVo storageVo = JSON.parseObject(projectToken, ProjectRedisStorageVo.class);

        if ("1".equals(ops)) {
            //保存到数据库，提交审核
            projectCreateService.saveProjectInfo(ProjectStatusEnume.FUNDING, storageVo);
            return AppResponse.ok("保存项目成功");
        } else if ("0".equals(ops)) {
            //保存到数据库，草稿状态
            projectCreateService.saveProjectInfo(ProjectStatusEnume.STARTING, storageVo);
            return AppResponse.ok("保存项目成功");
        } else {
            AppResponse<Object> fail = AppResponse.fail("保存项目失败");
            fail.setMsg("不支持该标记");
            return fail;
        }
    }


}
