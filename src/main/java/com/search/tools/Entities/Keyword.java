package com.search.tools.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Keyword {

    @TableId(value = "id", type = IdType.AUTO)
    private int    id;
    private String name;
    private int    searchTimes;
    private String status;
    private String createdAt;
    private String updatedAt;
}
