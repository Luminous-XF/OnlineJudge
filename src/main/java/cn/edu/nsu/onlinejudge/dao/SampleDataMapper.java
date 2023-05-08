package cn.edu.nsu.onlinejudge.dao;


import cn.edu.nsu.onlinejudge.entity.SampleData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SampleDataMapper {


    List<SampleData> selectSampleData(int problemId);

}
