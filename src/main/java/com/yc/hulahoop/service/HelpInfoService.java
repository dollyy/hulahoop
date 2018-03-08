package com.yc.hulahoop.service;

import com.yc.hulahoop.common.ServerResponse;
import com.yc.hulahoop.pojo.HelpInfo;

public interface HelpInfoService {

    ServerResponse list();

    ServerResponse add(HelpInfo helpInfo);

    ServerResponse update(HelpInfo helpInfo);

    ServerResponse delete(Integer helpInfoId);

}
