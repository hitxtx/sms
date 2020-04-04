package com.example.ms.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sys_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu", referencedColumnName = "id")
    private Menu parentMenu;

    @OneToMany(mappedBy = "parentMenu")
    private Set<Menu> submenus = new HashSet<>();

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_code")
    private String menuCode;

    @Column(name = "path")
    private String path;

    @Column(name = "icon")
    private String icon;

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