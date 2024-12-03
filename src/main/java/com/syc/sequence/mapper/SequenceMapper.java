package com.syc.sequence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.sequence.entity.Sequence;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SequenceMapper extends BaseMapper<Sequence> {

   default Sequence getForUpdate(String sequenceType){
       return selectOne(Wrappers.<Sequence>lambdaQuery().eq(Sequence::getSequenceType,sequenceType).last(" for update"));
   }
}
