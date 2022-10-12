package com.example.ms.module.system.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//  state: {
//    checked: true,
//    disabled: true,
//    expanded: true,
//    selected: true
//  },
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TreeNodeState {

    private Boolean checked = false;
    private Boolean disabled = false;
    private Boolean expanded = false;
    private Boolean selected = false;

}
