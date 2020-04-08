package com.example.ms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@EqualsAndHashCode(exclude = {"roles", "submenus"})
@ToString(exclude = {"roles", "submenus"})
@Entity
@Table(name = "sys_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu", referencedColumnName = "id")
    private Menu parentMenu;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "parentMenu")
    private Set<Menu> submenus = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles = new HashSet<>();

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
    private Date createdTime;

    @Column(name = "update_id")
    private String updateId;

    @Column(name = "updated_time")
    private Date updatedTime;

}