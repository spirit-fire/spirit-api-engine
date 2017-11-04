package com.spirit.engine.readability.service.impl;


import com.spirit.commons.common.ToolUtils;
import com.spirit.commons.qiniu.constant.QiniuDealTypeConstants;
import com.spirit.commons.qiniu.service.QiniuUploadService;
import com.spirit.engine.readability.HttpPageReader;
import com.spirit.engine.readability.PageReader;
import com.spirit.engine.readability.Readability;
import com.spirit.engine.readability.TikaCharsetDetector;
import com.spirit.engine.readability.model.ReadabilityCallBackModel;
import com.spirit.engine.readability.service.ReadabilityService;
import com.spirit.engine.readability.util.Patterns;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;

@Service("readabilitySercice")
public class ReadabilityServiceImpl implements ReadabilityService {

    /** readabiliry */
    private Readability readability = null;

    /** http page reader */
    private PageReader pageReader = null;


    /** init readability */
    private void init(){
        pageReader = new HttpPageReader();
        readability = new Readability();
        pageReader.setCharsetDetector(new TikaCharsetDetector());
        readability.setPageReader(pageReader);
        readability.setReadAllPages(false);
    }

    @Override
    public String getContextFromUrl(String url) throws Exception{
        if(null==readability || null==pageReader){
            readability = null;
            pageReader = null;
            this.init();
        }
        readability.processDocument(url);
        String content = readability.getArticleText();
        return content;
    }

    @Override
    public String getContextFromUrAndSavePicsToQiniu(String url) throws Exception {
        if(null==readability || null==pageReader){
            readability = null;
            pageReader = null;
            this.init();
        }
        readability.processDocument(url);
        String content = readability.getArticleText();
        ArrayList<String> list = Patterns.matchGroup(Patterns.PAGE_IMG_PATTERNS, content);
        for(String info:list){
//            String pid = MD5Utils.md5(info);
            String response = "";
            response = QiniuUploadService.upload(info, QiniuDealTypeConstants.QINIU_URL_TYPE);
//            content.replace(info, pid);
            content = content.replace(info, response);
        }
        return content;
    }

    @Override
    public ReadabilityCallBackModel getCallBackModerlFromUrAndSavePicsToQiniu(String url) throws Exception {
        ReadabilityCallBackModel callBackModel = new ReadabilityCallBackModel();
        if(null==readability || null==pageReader){
            readability = null;
            pageReader = null;
            this.init();
        }
        readability.processDocument(url);
        String content = readability.getArticleText();
        ArrayList<String> list = Patterns.matchGroup(Patterns.PAGE_IMG_PATTERNS, content);
        ArrayList<String> pidList = new ArrayList<String>();
        String host = getHost(url);
        for(String info:list){
//            String pid = MD5Utils.md5(info);
            String response = "";
            info = buildPicUrl(host, info);
            response = QiniuUploadService.upload(info, QiniuDealTypeConstants.QINIU_URL_TYPE);
//            content.replace(info, pid);
            if(ToolUtils.isStringEmpty(response)){
                continue;
            }
            content = content.replace(info, response);
            pidList.add(response);
        }
        callBackModel.setText(URLEncoder.encode(content, "UTF-8"));
        callBackModel.setPids(ToolUtils.implode(pidList,","));
        return callBackModel;
    }

    private static String getHost(String url){
        int index = (url.startsWith("http://")) ? 8 : 9;
        String host = url.substring(0, url.indexOf('/',index));
        return host;
    }

    private static String buildPicUrl(String host, String picurl){
        if(picurl.startsWith("http")){
            return picurl;
        }
        String prefix = (picurl.startsWith("/")) ? "" : "/";
        return host + prefix + picurl;
    }

}
