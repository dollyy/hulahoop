package com.yc.hulahoop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.CityMapper;
import com.yc.hulahoop.dao.HelpInfoMapper;
import com.yc.hulahoop.pojo.City;
import com.yc.hulahoop.pojo.HelpInfo;
import com.yc.hulahoop.service.HelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("helpInfoService")
public class HelpInfoServiceImpl implements HelpInfoService {

    @Autowired
    private HelpInfoMapper helpInfoMapper;

    @Autowired
    CityMapper cityMapper;

    @Override
    public ServerResponse list(Integer pageNum) {
        List<HelpInfo> helpInfoList = helpInfoMapper.list();
        if (helpInfoList.size() == 0) {
            return ServerResponse.createBySuccessMessage("暂无数据");
        }
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.PAGE_SIZE);
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(helpInfoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse listByUpload() {
        Map<String, Object> result= Maps.newHashMap();
        //帮助信息
        List<HelpInfo> helpInfoList = helpInfoMapper.listByUpload();
        result.put("helpInfo",helpInfoList);
        //城市信息
        List<City> cityList= cityMapper.selectCities();
        result.put("cities",cityList);
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse add(HelpInfo helpInfo) {
        int count = helpInfoMapper.insert(helpInfo);
        if (count > 0) {
            return ServerResponse.createBySuccessData(helpInfo.getId());
        }
        return ServerResponse.createByErrorMessage("新增帮助信息失败");
    }

    @Override
    public ServerResponse update(HelpInfo helpInfo) {
        //禁止修改的字段
        helpInfo.setCreateTime(null);
        int count = helpInfoMapper.updateByPrimaryKeySelective(helpInfo);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新帮助信息成功");
        }
        return ServerResponse.createByErrorMessage("更新帮助信息失败");
    }

    @Override
    public ServerResponse delete(Integer helpInfoId) {
        int count = helpInfoMapper.deleteByPrimaryKey(helpInfoId);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("删除帮助信息成功");
        }
        return ServerResponse.createByErrorMessage("删除帮助信息失败");
    }

}
