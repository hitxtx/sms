package com.example.ms.module.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.*;

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
    @ManyToOne
    @JoinColumn(name = "parent_menu", referencedColumnName = "id")
    private Menu parentMenu;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "parentMenu")
    private List<Menu> submenus = new ArrayList<>();

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

    @Column(name = "deleted_flag")
    private Boolean deletedFlag;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

}
