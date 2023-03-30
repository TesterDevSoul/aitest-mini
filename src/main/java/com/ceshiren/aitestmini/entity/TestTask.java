package com.ceshiren.aitestmini.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;

/**
 * 表名：test_task
 * 表注释：测试任务表
*/
@Table(name = "test_task")
@ToString
@Getter
@Setter
public class TestTask extends BaseEntityNew {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * Jenkins的构建url
     */
    @Column(name = "build_url")
    private String buildUrl;



    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    private Integer status;

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
     * Jenkins执行测试时的命令脚本
     */
    @Column(name = "test_command")
    private String testCommand;

    @Column(name = "job_id")
    private Integer jobId;
}