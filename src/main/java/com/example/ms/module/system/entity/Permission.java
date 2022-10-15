package com.example.ms.module.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@EqualsAndHashCode(exclude = {"roles", "id", "updatedTime", "createdTime"})
@ToString(exclude = {"roles"})
@Entity
@Table(name = "sms_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    @Column(name = "module")
    private String module;

    @Column(name = "tag")
    private String tag;

    @Column(name = "path")
    private String path;

    @Column(name = "deleted_flag")
    private Boolean deletedFlag;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;


}
