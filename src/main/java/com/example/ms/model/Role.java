package com.example.ms.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@Table(name = "sys_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "sort")
    private Long sort;

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