package com.coffee.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 咖啡豆分级与缺陷信息实体类
 * 对应数据表：coffee_bean_grade_info
 */
@Data
@TableName("coffee_bean_grade_info")
public class CoffeeBeanGradeInfo {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 豆类名称，如A级豆、AA级豆、黑豆等 */
    private String beanName;

    /** 英文名，如 Grade A, Black Bean */
    private String englishName;

    /** 核心识别结果，如 精品豆、缺陷豆 */
    private String recognitionResult;

    /** 分类：等级豆、缺陷豆或特殊豆 */
    private String category;

    /** 外观特征描述 */
    private String appearance;

    /** 产生原因 */
    private String cause;

    /** 对风味的影响 */
    private String flavorImpact;

    /** 处理建议 */
    private String handlingSuggestion;

    /** 预防措施 */
    private String prevention;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 样例照片*/
    private String sampleImageUrl;
}
