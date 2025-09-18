package com.coffee.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.coffee.project.domain.User;

/**
 * UserMapper 是 MyBatis-Plus 提供的 Mapper 接口，负责操作用户表（user 表）
 * Mapper 层也称为 DAO（Data Access Object）层，专门负责数据库的增删改查操作
 *
 * 继承 BaseMapper<User>，就拥有了常用的数据库操作方法（如插入、删除、更新、查询）
 * 无需手动编写 SQL，MyBatis-Plus 会自动生成对应的 SQL 语句
 *
 * 也可以在此接口中定义自定义的数据库操作方法，结合 XML 或注解实现更复杂的查询
 */
@Mapper  // 表示这是一个 MyBatis 的 Mapper 接口，Spring 容器会扫描并自动创建实现类
public interface UserMapper extends BaseMapper<User> {
    // 这里继承了 BaseMapper<User>，默认就具备了很多 CRUD（增删改查）方法
    // 例如：
    //  - int insert(User user);    // 插入用户
    //  - int deleteById(Long id);  // 根据 ID 删除用户
    //  - User selectById(Long id); // 根据 ID 查询用户
    //  - int updateById(User user); // 根据 ID 更新用户
    //
    // 如果需要自定义方法，可以在这里声明，再写对应的 XML 或注解 SQL
}
