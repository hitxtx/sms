package com.example.ms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude = {"roles"})
@ToString(exclude = {"roles"})
@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "id_card_no")
    private String idCardNo;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "count")
    private Integer count;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "login_time")
    private Date loginTime;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "remark")
    private String remark;

    @Column(name = "deleted_flag")
    private Boolean deletedFlag;

    @Column(name = "create_id")
    private String createId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "update_id")
    private String updateId;

    @Column(name = "updated_time")
    private Date updatedTime;
}