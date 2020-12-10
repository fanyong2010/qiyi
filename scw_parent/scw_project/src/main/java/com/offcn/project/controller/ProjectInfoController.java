package com.offcn.project.controller;

import com.offcn.dycommon.response.AppResponse;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.req.ProjectDetailVo;
import com.offcn.project.vo.req.ProjectVo;
import com.offcn.utils.OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(tags = "项目基本功能-文件上传")
@Slf4j
public class ProjectInfoController {

    @Autowired
    private OssUtil ossUtil;

    @Autowired
    private ProjectInfoService projectInfoService;


    @ApiOperation("文件上传功能")
    @PostMapping("/upload")
    // Map<String, Object>
    // 参数：spring mvc 的多媒体类，可以用来文件上传
    public AppResponse upload(MultipartFile[] files) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        try {
            if(files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String url = ossUtil.upload(file.getInputStream(), file.getOriginalFilename());
                    list.add(url);
                }
            }
            map.put("urls", list);
            return AppResponse.ok(map);
        } catch (IOException e) {
            e.printStackTrace();
            AppResponse<Map<String, Object>> fail = AppResponse.fail(map);
            fail.setMsg("图片上传失败");
            return fail;
        }
    }


    @ApiOperation("获取单个回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> getTReturn(@PathVariable("returnId") Integer returnId){
        TReturn tReturn = projectInfoService.getReturnInfo(returnId);
        return AppResponse.ok(tReturn);
    }


    @ApiOperation("获取系统所有项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> all() {
        List<TProject> allPros = projectInfoService.getAllProjects();

        List<ProjectVo> proVoList = new ArrayList<>();

        for (TProject allPro : allPros) {
            ProjectVo vo = new ProjectVo();
            BeanUtils.copyProperties(allPro, vo);

            // 获取该项目对应的头图
            List<TProjectImages> proImgList = projectInfoService.getProjectImages(allPro.getId());
            for (TProjectImages proImg : proImgList) {
                if(proImg.getImgtype().byteValue() == 0) {
                    vo.setHeaderImage(proImg.getImgurl());
                }
            }
            proVoList.add(vo);
        }
        return AppResponse.ok(proVoList);
    }


    @ApiOperation("获取项目回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> detailsReturn(@PathVariable("projectId") Integer projectId) {
        List<TReturn> returns = projectInfoService.getProjectReturns(projectId);
        return AppResponse.ok(returns);
    }


    @ApiOperation("获取项目详情信息")
    @GetMapping("/details/info/{projectId}")
    public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId) {
        ProjectDetailVo proDetailVo = new ProjectDetailVo();

        // 1. 获取项目基本信息
        TProject projectInfo = projectInfoService.getProjectInfo(projectId);
        BeanUtils.copyProperties(projectInfo, proDetailVo);

        // 2. 项目图片信息
        List<TProjectImages> proImgList = projectInfoService.getProjectImages(projectId);

        List<String> detailImgList = new ArrayList<>();
        for (TProjectImages proImg : proImgList) {
            // 头图
            if(proImg.getImgtype().byteValue() == 0) {
                proDetailVo.setHeaderImage(proImg.getImgurl());
            } else {
                detailImgList.add(proImg.getImgurl());
            }
        }
        proDetailVo.setDetailsImage(detailImgList);

        // 3. 项目的回报信息
        List<TReturn> returnList = projectInfoService.getProjectReturns(projectId);
        proDetailVo.setProjectReturns(returnList);

        return AppResponse.ok(proDetailVo);

    }


    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/tags")
    public AppResponse<List<TTag>> tags() {
        List<TTag> tags = projectInfoService.getAllProjectTags();
        return AppResponse.ok(tags);
    }

    @ApiOperation("获取系统所有的项目分类")
    @GetMapping("/types")
    public AppResponse<List<TType>> types() {
        List<TType> types = projectInfoService.getProjectTypes();
        return AppResponse.ok(types);
    }

}
