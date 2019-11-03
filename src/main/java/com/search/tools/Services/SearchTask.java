/**************************************************************************************************
 * Copyright (c) 2019. Abu all rights reserved.                                                   *
 **************************************************************************************************/

package com.search.tools.Services;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.search.tools.Entities.Keyword;
import com.search.tools.Entities.SearchLog;
import com.search.tools.Mappers.KeywordMapper;
import com.search.tools.Mappers.SearchLogMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Component
public class SearchTask {

    @Autowired
    private KeywordMapper   keywordMapper;
    @Autowired
    private SearchLogMapper searchLogMapper;

    @Scheduled(fixedRate = 30 * 1000)
    public void test() throws IOException {
        System.out.println("[ " + "当前时间 : " + new Date() + " ]");
        int count = keywordMapper.selectCount(new QueryWrapper<Keyword>());
        if (count < 1) {
            System.out.println("没有关键词停止搜索");
            return;
        }

        QueryWrapper<Keyword> query = new QueryWrapper<Keyword>();
        query.select().orderByAsc("search_times").last("LIMIT 0,1");

        Keyword keyword = keywordMapper.selectOne(query);
        System.out.println(keyword.toString());

        String       url          = "http://www.google.com.ph/search?&q=" + keyword.getName();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request      request      = new Request.Builder().url(url).build();
        Response     response     = okHttpClient.newCall(request).execute();

        String datetime = DateUtil.now();

        Keyword upKeyword = new Keyword();
        upKeyword.setId(keyword.getId());
        upKeyword.setSearchTimes(keyword.getSearchTimes() + 1);
        upKeyword.setUpdatedAt(datetime);
        keywordMapper.updateById(upKeyword);

        SearchLog searchLog = new SearchLog();
        searchLog.setUrl(url);
        searchLog.setResultClip(StringEscapeUtils.escapeHtml4(Objects.requireNonNull(response.body()).string()
                                                                     .substring(0, 120)));
        searchLog.setHttpStatusCode(response.code());
        searchLog.setSearchEngine("Google.ph");
        searchLog.setCreatedAt(datetime);
        searchLog.setUpdatedAt(datetime);
        searchLog.setKeyword(keyword.getName());
        searchLogMapper.insert(searchLog);
    }
}
