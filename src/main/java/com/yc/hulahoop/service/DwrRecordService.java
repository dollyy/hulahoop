package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;

public interface DwrRecordService {

    boolean insertComment(String message, Integer userId);

    ServerResponse update(Integer dwrId);

}
