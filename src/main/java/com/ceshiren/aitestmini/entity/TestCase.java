package com.ceshiren.aitestmini.entity;

import lombok.ToString;

import java.util.Date;
import javax.persistence.*;

/**
 * 表名：test_case
 * 表注释：测试用例表
*/
@Table(name = "test_case")
@ToString
public class TestCase extends BaseEntityNew {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用例名称
     */
    @Column(name = "case_name")
    private String caseName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 测试用例内容
     */
    @Column(name = "case_data")
    private String caseData;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用例名称
     *
     * @return caseName - 用例名称
     */
    public String getCaseName() {
        return caseName;
    }

    /**
     * 设置用例名称
     *
     * @param caseName 用例名称
     */
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return updateTime - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取测试用例内容
     *
     * @return caseData - 测试用例内容
     */
    public String getCaseData() {
        return caseData;
    }

    /**
     * 设置测试用例内容
     *
     * @param caseData 测试用例内容
     */
    public void setCaseData(String caseData) {
        this.caseData = caseData;
    }
}