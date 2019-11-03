/**************************************************************************************************
 * Copyright (c) 2019. Abu all rights reserved.                                                   *
 **************************************************************************************************/

package com.search.tools.Mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.search.tools.Entities.Keyword;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface KeywordMapper extends BaseMapper<Keyword> {
}
