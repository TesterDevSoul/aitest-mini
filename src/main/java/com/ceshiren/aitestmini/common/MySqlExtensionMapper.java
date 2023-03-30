package com.ceshiren.aitestmini.common;


import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MySqlExtensionMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
}
