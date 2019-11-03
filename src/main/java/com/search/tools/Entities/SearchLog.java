/**************************************************************************************************
 * Copyright (c) 2019. Abu all rights reserved.                                                   *
 **************************************************************************************************/

package com.search.tools.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName(value = "search_log")
@Data
public class SearchLog {

    @TableId(value = "id", type = IdType.AUTO)
    private int    id;
    private String searchEngine;
    private String keyword;
    private int    httpStatusCode;
    private String url;
    private String resultClip;
    private String createdAt;
    private String updatedAt;
}
