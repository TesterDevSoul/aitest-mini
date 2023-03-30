package com.ceshiren.aitestmini.entity;

import javax.persistence.*;

/**
 * 表名：test_task_case_rel
 * 表注释：测试任务表
*/
@Table(name = "test_task_case_rel")
public class TestTaskCaseRel extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 用例id
     */
    @Column(name = "case_id")
    private Integer caseId;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取任务id
     *
     * @return taskId - 任务id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置任务id
     *
     * @param taskId 任务id
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取用例id
     *
     * @return caseId - 用例id
     */
    public Integer getCaseId() {
        return caseId;
    }

    /**
     * 设置用例id
     *
     * @param caseId 用例id
     */
    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }
}