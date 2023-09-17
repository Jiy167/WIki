package com.jiyuan.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiyuan.wiki.domain.User;
import com.jiyuan.wiki.domain.UserExample;
import com.jiyuan.wiki.exception.BusinessException;
import com.jiyuan.wiki.exception.BusinessExceptionCode;
import com.jiyuan.wiki.mapper.UserMapper;
import com.jiyuan.wiki.req.UserQueryReq;
import com.jiyuan.wiki.req.UserSaveReq;
import com.jiyuan.wiki.resp.UserQueryResp;
import com.jiyuan.wiki.resp.PageResp;
import com.jiyuan.wiki.util.CopyUtil;
import com.jiyuan.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("total row：{}", pageInfo.getTotal());
        LOG.info("total page：{}", pageInfo.getPages());

        // List<UserResp> respList = new ArrayList<>();
        // for (User user : userList) {
        //     // UserResp userResp = new UserResp();
        //     // BeanUtils.copyProperties(user, userResp);
        //     // object copy
        //     UserResp userResp = CopyUtil.copy(user, UserResp.class);
        //
        //     respList.add(userResp);
        // }

        // list copy
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    /**
     * save
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            User userDB = selectByLoginName(req.getLoginName());
            if (ObjectUtils.isEmpty(userDB)) {
                // add
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                // loginName already exists
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // update
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }
}
