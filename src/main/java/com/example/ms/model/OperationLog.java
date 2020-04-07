package com.example.ms.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "sys_operation_log")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account")
    private String account;

    @Column(name = "action_path")
    private String actionPath;

    @Column(name = "action_desc")
    private String actionDesc;

    @Column(name = "remark")
    private String remark;

    @Column(name = "deleted_flag")
    private Boolean deletedFlag;

    @Column(name = "create_id")
    private String createId;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "update_id")
    private String updateId;

    @Column(name = "updated_time")
    private Timestamp updatedTime;
}