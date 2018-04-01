package com.yc.hulahoop.dao;

import com.yc.hulahoop.pojo.DwrRecord;
import com.yc.hulahoop.vo.DwrRecordVo;

import java.util.List;

public interface DwrRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DwrRecord record);

    int insertSelective(DwrRecord record);

    DwrRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DwrRecord record);

    int updateByPrimaryKey(DwrRecord record);

    List<DwrRecordVo> listByUser(Integer userId);

    int updateStatusByDwrId(Integer dwrId);

    int queryNotice(Integer userId);

}