package com.jiyuan.wiki.service;

import com.jiyuan.wiki.domain.Test;
import com.jiyuan.wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestServive {

    @Resource
    private TestMapper testMapper;
    public List<Test> list(){
        return testMapper.list1();
    }
}
