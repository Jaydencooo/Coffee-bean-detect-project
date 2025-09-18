package com.coffee.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coffee.project.domain.DetectionRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * DetectionRecordMapper 是 MyBatis-Plus 提供的 Mapper 接口，
 * 用于操作数据库中的 detection_record 表（检测记录表）。
 *
 * Mapper 层，也叫 DAO（数据访问对象）层，负责和数据库直接交互，
 * 执行增删改查等操作。
 *
 * 继承 BaseMapper<DetectionRecord>，自动拥有 MyBatis-Plus 提供的通用 CRUD 方法，
 * 包括插入（insert）、删除（delete）、更新（update）、查询（select）等，
 * 不需要写任何 SQL 或方法实现。
 */
@Mapper  // 标记为 MyBatis 的 Mapper，Spring 会自动扫描并创建代理实现类
public interface DetectionRecordMapper extends BaseMapper<DetectionRecord> {
    // 因为继承了 BaseMapper，所有基本的数据库操作都已经具备了
    // 例如：
    //   int insert(DetectionRecord record);
    //   int deleteById(Long id);
    //   DetectionRecord selectById(Long id);
    //   int updateById(DetectionRecord record);
    //
    // 如果需要自定义复杂查询，可以在这里添加方法声明，然后写对应的 XML 或注解 SQL
}
