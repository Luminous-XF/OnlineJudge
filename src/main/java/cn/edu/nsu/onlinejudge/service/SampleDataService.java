package cn.edu.nsu.onlinejudge.service;


import cn.edu.nsu.onlinejudge.dao.SampleDataMapper;
import cn.edu.nsu.onlinejudge.entity.SampleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleDataService {

    @Autowired
    private SampleDataMapper sampleDataMapper;


    public List<SampleData> findSampleData(int problemId) {
        return sampleDataMapper.selectSampleData(problemId);
    }
}
