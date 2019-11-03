package com.search.tools.Services;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.search.tools.Entities.Keyword;
import com.search.tools.Mappers.KeywordMapper;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Service
public class DBHandler {

    @Autowired
    private KeywordMapper keywordMapper;

    @Async("taskExecutor")
    public void fileInsertToDB(String path) throws Exception {
        String encoding = UniversalDetector.detectCharset(Paths.get(path));

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert inputStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        keywordMapper.delete(new QueryWrapper<Keyword>().select());
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            String  ss      = new String(str.getBytes(encoding), StandardCharsets.UTF_8);
            Keyword keyword = new Keyword();
            keyword.setName(str);
            keyword.setCreatedAt(DateUtil.now());

            keywordMapper.insert(keyword);
        }

        inputStream.close();
        bufferedReader.close();
    }
}