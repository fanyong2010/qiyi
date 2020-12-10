package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.discovery.converters.Auto;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;


    @Override
    public String initCreateProject(Integer memberId) {

        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        vo.setMemberid(memberId);

        // 充当 项目没有保存到数据库之前 的临时id
        String proToken = UUID.randomUUID().toString().replace("-", "");
        String voStr = JSON.toJSONString(vo);

        stringRedisTemplate.opsForValue().set(proToken, voStr, 7, TimeUnit.DAYS);

        return proToken;
    }

    /**
     * 将项目信息保存到数据库中
     * @param auth  项目状态信息
     * @param proRedisVo  项目全部信息
     */
    @Override
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo proRedisVo) {
        // 1. 保存 t_project 表的信息
        TProject project = new TProject();
        BeanUtils.copyProperties(proRedisVo, project);
        project.setCreatedate(DateUtil.getFormatTime());
        project.setStatus(auth.getStatus());
        projectMapper.insert(project);

        // *************以下表和t_project都是有一对多的关系：需要配置t_project的id回显
        Integer proId = project.getId();

        // 2. 保存 t_project_images 表的信息
        // 2.1 保存大图
        String headImgUrl = proRedisVo.getHeaderImage();
        TProjectImages headImg = new TProjectImages(null, proId, headImgUrl, ProjectImageTypeEnume.HEADER.getCode());
        projectImagesMapper.insert(headImg);
        // 2.2. 保存详细图
        List<String> detailImgUrls = proRedisVo.getDetailsImage();
        for (String detailImgUrl : detailImgUrls) {
            TProjectImages detailImg = new TProjectImages(null, proId, headImgUrl, ProjectImageTypeEnume.DETAILS.getCode());
            projectImagesMapper.insert(detailImg);
        }

        // 3. 保存 t_project_tag 表的信息
        List<Integer> tagids = proRedisVo.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tag = new TProjectTag(null, proId, tagid);
            projectTagMapper.insert(tag);
        }

        // 4. 保存 t_project_type 表的信息
        List<Integer> typeids = proRedisVo.getTypeids();
        for (Integer typeid : typeids) {
            TProjectType type = new TProjectType(null, proId, typeid);
            projectTypeMapper.insert(type);
        }

        // 5. 保存 t_return 表的信息
        List<TReturn> proReturns = proRedisVo.getProjectReturns();
        for (TReturn tReturn : proReturns) {
            tReturn.setProjectid(proId);
            returnMapper.insert(tReturn);
        }

        //6、删除临时数据
        stringRedisTemplate.delete(proRedisVo.getProjectToken());
    }


}
