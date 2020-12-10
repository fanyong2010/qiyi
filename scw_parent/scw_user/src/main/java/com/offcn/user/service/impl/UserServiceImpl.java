package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.po.TMemberAddressExample;
import com.offcn.user.po.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private TMemberAddressMapper memberAddressMapper;

    @Override
    public void registerUser(TMember member) {
        // 1. 判断用户输入的手机号是否存在
        TMemberExample ex = new TMemberExample();
        ex.createCriteria().andLoginacctEqualTo(member.getLoginacct());

        long num = memberMapper.countByExample(ex);
        if(num > 0) {
            // 2. 如果存在：抛出异常
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        else {
            // 3. 如果不存在：插入到数据库中

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encode = encoder.encode(member.getUserpswd());

            //设置密码
            member.setUserpswd(encode);

            member.setUsername(member.getLoginacct());

            //实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
            member.setAuthstatus("0");
            //用户类型: 0 - 个人， 1 - 企业
            member.setUsertype("0");
            //账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
            member.setAccttype("2");

            memberMapper.insert(member);
        }

    }

    @Override
    public TMember login(String username, String password) {

        TMemberExample ex = new TMemberExample();
        ex.createCriteria().andLoginacctEqualTo(username);

        List<TMember> list = memberMapper.selectByExample(ex);

        if(list != null && list.size() > 0) {
            TMember member = list.get(0);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(password, member.getUserpswd());

            return matches ? member : null;

        } else {
            return null;
        }

    }

    /**
     * 获取用户收货地址
     *
     * @param memberId
     * @return
     */
    @Override
    public List<TMemberAddress> addressList(Integer memberId) {
        TMemberAddressExample ex = new TMemberAddressExample();
        ex.createCriteria().andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(ex);
    }

}
