package com.yc.hulahoop.service.impl;

import com.yc.hulahoop.common.Const;
import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.dao.DwrRecordMapper;
import com.yc.hulahoop.pojo.DwrRecord;
import com.yc.hulahoop.service.DwrRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dwrRecordService")
public class DwrRecordServiceImpl implements DwrRecordService {

    @Autowired
    DwrRecordMapper dwrRecordMapper;

    @Override
    public boolean insertComment(String message, Integer userId) {
        String[] info = message.split("@=");
        DwrRecord dwrRecord = new DwrRecord();
        dwrRecord.setResponseId(Integer.parseInt(info[0]));
        dwrRecord.setRequestId(userId);
        if (info[2].contains(".")) {
            int len = info[2].indexOf(".");
            dwrRecord.setCommentSequence(Integer.parseInt(info[2].substring(0, len)));
        } else {
            dwrRecord.setCommentSequence(Integer.parseInt(info[2]));
        }
        dwrRecord.setContent(info[1]);
        int count = dwrRecordMapper.insert(dwrRecord);
        return count > 0;
    }

    @Override
    public ServerResponse update(Integer dwrId) {
        if(dwrId == null){
            return ServerResponse.createByErrorCodeMessage(Const.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    Const.ResponseCode.ILLEGAL_PARAMETER.getDescription());
        }
        int count=dwrRecordMapper.updateStatusByDwrId(dwrId);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }
}
