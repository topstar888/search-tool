package com.search.tools.Controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.search.tools.Entities.Keyword;
import com.search.tools.Entities.SearchLog;
import com.search.tools.Mappers.KeywordMapper;
import com.search.tools.Mappers.SearchLogMapper;
import com.search.tools.Services.DBHandler;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class IndexController {

    private final SearchLogMapper searchLogMapper;
    private final KeywordMapper   keywordMapper;
    private final DBHandler       dbHandler;

    public IndexController(SearchLogMapper searchLogMapper, KeywordMapper keywordMapper, DBHandler dbHandler) {
        this.searchLogMapper = searchLogMapper;
        this.keywordMapper   = keywordMapper;
        this.dbHandler       = dbHandler;
    }

    @RequestMapping(path = "/", method = {RequestMethod.GET})
    public String index(ModelMap modelMap) {
        QueryWrapper<SearchLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().orderByDesc("id");
        IPage<SearchLog> page    = new Page<>(1, 20);
        IPage<SearchLog> logList = searchLogMapper.selectPage(page, queryWrapper);

        modelMap.addAttribute("records", logList.getRecords());
        modelMap.addAttribute("pages", logList.getPages());
        modelMap.addAttribute("size", logList.getSize());
        modelMap.addAttribute("total", logList.getTotal());
        modelMap.addAttribute("current", logList.getCurrent());

        List<Integer> nums = new ArrayList<>();
        int           i    = 1;
        while (i <= logList.getPages()) {
            nums.add(i);
            i++;
        }

        modelMap.addAttribute("pageList", nums);

        QueryWrapper<Keyword> qy    = new QueryWrapper<Keyword>().select();
        IPage<Keyword>        kPage = new Page<>(1, 20);
        IPage<Keyword>        kList = keywordMapper.selectPage(kPage, qy);
        modelMap.addAttribute("keyRecords", kList.getRecords());
        modelMap.addAttribute("keyPages", kList.getPages());
        modelMap.addAttribute("keyCurrent", kList.getCurrent());

        List<Integer> kNums = new ArrayList<>();
        int           ki    = 1;
        while (ki <= kList.getPages()) {
            kNums.add(ki);
            ki++;
        }

        modelMap.addAttribute("kPageList", kNums);

        return "index";
    }

    @RequestMapping(path = "/page/{curPage}", method = {RequestMethod.GET})
    public String page(@PathVariable(value = "curPage", required = false) int cur, ModelMap modelMap) {
        QueryWrapper<SearchLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select().orderByDesc("id");
        IPage<SearchLog> page    = new Page<>(cur, 20);
        IPage<SearchLog> logList = searchLogMapper.selectPage(page, queryWrapper);
//        String jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(logList);
        modelMap.addAttribute("records", logList.getRecords());
        modelMap.addAttribute("pages", logList.getPages());
        modelMap.addAttribute("size", logList.getSize());
        modelMap.addAttribute("total", logList.getTotal());
        modelMap.addAttribute("current", logList.getCurrent());

        List<Integer> nums = new ArrayList<>();
        int           i    = 1;
        while (i <= logList.getPages()) {
            nums.add(i);
            i++;
        }

        modelMap.addAttribute("pageList", nums);


        QueryWrapper<Keyword> qy    = new QueryWrapper<Keyword>().select();
        IPage<Keyword>        kPage = new Page<>(1, 20);
        IPage<Keyword>        kList = keywordMapper.selectPage(kPage, qy);
        modelMap.addAttribute("keyRecords", kList.getRecords());
        modelMap.addAttribute("keyPages", kList.getPages());
        modelMap.addAttribute("keyCurrent", kList.getCurrent());

        List<Integer> kNums = new ArrayList<>();
        int           ki    = 1;
        while (ki <= kList.getPages()) {
            kNums.add(ki);
            ki++;
        }

        modelMap.addAttribute("kPageList", kNums);


        return "index";
    }

    @PostMapping(value = "/upload-keywords")
    public String upload(@RequestPart(value = "uploadFile") MultipartFile file, ModelMap modelMap) throws Exception {
        if (file.isEmpty()) {
            modelMap.addAttribute("message", "文件为空");
            return "message";
        }

        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (!suffixName.equals(".txt")) {
            System.out.println(suffixName);
            modelMap.addAttribute("message", "目前只支持txt一种后缀");
            return "message";
        }

        File saveFile = new File("keyword.txt");

        FileOutputStream     fos    = new FileOutputStream(saveFile.getAbsolutePath());
        BufferedOutputStream stream = new BufferedOutputStream(fos);
        stream.write(file.getBytes());
        stream.flush();

        dbHandler.fileInsertToDB(saveFile.getAbsolutePath());

        modelMap.addAttribute(
                "message",
                "上传成功。系统将在后台自动分析关键词并逐一标记入库，" +
                "可能需要一点点时间。完成后将自动启动查询任务，" +
                "届时将能够在首页看到具体的任务执行记录。"
                             );
        return "message";
    }

    @GetMapping(value = "/delete-logs")
    public String deleteLogs(ModelMap modelMap) {
        QueryWrapper<SearchLog> queryWrapper = new QueryWrapper<SearchLog>();
        queryWrapper.select();
        int count = searchLogMapper.delete(queryWrapper);
        modelMap.addAttribute("message", "删除了：" + count);
        return "message";
    }

    @GetMapping(value = "/delete-keyword")
    public String deleteKeywords(ModelMap modelMap) {
        QueryWrapper<Keyword> queryWrapper = new QueryWrapper<Keyword>();
        queryWrapper.select();
        int count = keywordMapper.delete(queryWrapper);
        modelMap.addAttribute("message", "删除了：" + count);
        return "message";
    }

}
