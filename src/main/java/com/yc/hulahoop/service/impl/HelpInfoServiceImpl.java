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
import org.apache.commons.lang3.StringUtils;
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
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.ADMIN_HELPINFO_PAGESIZE);
        List<HelpInfo> helpInfoList = helpInfoMapper.list();
        if (helpInfoList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(helpInfoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }

    @Override
    public ServerResponse listByUpload() {
        Map<String, Object> result = Maps.newHashMap();
        //帮助信息
        List<HelpInfo> helpInfoList = helpInfoMapper.listByUpload();
        if(helpInfoList.size() > 0){
            result.put("helpInfo", helpInfoList);
        }
        //城市信息
        List<City> cityList = cityMapper.selectCities();
        if(cityList.size() > 0){
            result.put("cities", cityList);
        }
        return ServerResponse.createBySuccessData(result);
    }

    @Override
    public ServerResponse add(HelpInfo helpInfo) {
        if(helpInfo == null){
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //新增帮助信息
        int count = helpInfoMapper.insert(helpInfo);
        //新增帮助信息成功并返回id
        if (count > 0) {
            return ServerResponse.createBySuccessData(helpInfo.getId());
        }
        //新增帮助信息失败
        return ServerResponse.createByErrorMessage("新增帮助信息失败");
    }

    @Override
    public ServerResponse update(HelpInfo helpInfo) {
        if(helpInfo == null){
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //禁止修改的字段
        helpInfo.setCreateTime(null);
        //修改帮助信息
        int count = helpInfoMapper.updateByPrimaryKeySelective(helpInfo);
        //更新帮助信息成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("更新帮助信息成功");
        }
        //更新帮助信息失败
        return ServerResponse.createByErrorMessage("更新帮助信息失败");
    }

    @Override
    public ServerResponse delete(Integer helpInfoId) {
        if(helpInfoId == null){
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //删除帮助信息
        int count = helpInfoMapper.deleteByPrimaryKey(helpInfoId);
        //删除帮助信息成功
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("删除帮助信息成功");
        }
        //删除帮助信息失败
        return ServerResponse.createByErrorMessage("删除帮助信息失败");
    }

    @Override
    public ServerResponse search(Integer pageNum, String content) {
        if (StringUtils.isBlank(content)) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        //1.startPage--start
        PageHelper.startPage(pageNum, Const.ADMIN_HELPINFO_PAGESIZE);
        //查询
        List<HelpInfo> helpInfoList = helpInfoMapper.search("%" + content + "%");
        if (helpInfoList.size() == 0) {
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.NO_INFO.getCode(),
                    Const.ResponseCode.NO_INFO.getDescription());
        }
        //2.pageHelper--end
        PageInfo pageInfo = new PageInfo(helpInfoList);
        return ServerResponse.createBySuccessData(pageInfo);
    }
}
