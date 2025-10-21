package com.coffee.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // 自动生成所有属性的 getter/setter、toString、equals 等方法
@NoArgsConstructor // 自动生成无参构造函数
@AllArgsConstructor // 自动生成全参构造函数
public class CoffeeBeanGradeInfoVO {

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

}