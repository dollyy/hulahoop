package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.HelpInfoMapper;
import com.yc.hulahoop.pojo.HelpInfo;
import com.yc.hulahoop.service.HelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("helpInfoService")
public class HelpInfoServiceImpl implements HelpInfoService {

    @Autowired
    private HelpInfoMapper helpInfoMapper;

    @Override
    public ServerResponse list() {
        List<HelpInfo> helpInfoList = helpInfoMapper.list();
        if (helpInfoList.size() == 0) {
            return ServerResponse.createBySuccessMessage("暂无数据");
        }
        return ServerResponse.createBySuccessData(helpInfoList);
    }

    @Override
    public ServerResponse add(HelpInfo helpInfo) {
        int count = helpInfoMapper.insert(helpInfo);
        if (count > 0) {
            return ServerResponse.createBySuccessMessage("新增帮助信息成功");
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
