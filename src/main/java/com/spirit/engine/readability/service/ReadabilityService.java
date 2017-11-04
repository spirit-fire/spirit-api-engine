package com.spirit.engine.readability.service;


import com.spirit.engine.readability.model.ReadabilityCallBackModel;

public interface ReadabilityService {

    String getContextFromUrl(String url) throws Exception;

    String getContextFromUrAndSavePicsToQiniu(String url) throws Exception;

    ReadabilityCallBackModel getCallBackModerlFromUrAndSavePicsToQiniu(String url) throws Exception;

}
