package com.example.ms.model.bo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sys_log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account")
    private String account;

    @Column(name = "path")
    private String path;

    @Column(name = "description")
    private String description;

    @Column(name = "created_time")
    private Date createdTime;

}