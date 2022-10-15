package com.example.ms.module.system.model;

import lombok.Data;

import java.util.List;

// {
//  text: "Node 1",
//  icon: "glyphicon glyphicon-stop",
//  selectedIcon: "glyphicon glyphicon-stop",
//  color: "#000000",
//  backColor: "#FFFFFF",
//  href: "#node-1",
//  selectable: true,
//  state: {
//    checked: true,
//    disabled: true,
//    expanded: true,
//    selected: true
//  },
//  tags: ['available'],
//  nodes: [
//    {},
//    ...
//  ]
// }
@Data
public class TreeNode {

    private Long id;
    private String text;
//    private String icon = "glyphicon glyphicon-stop";
//    private String selectedIcon = "glyphicon glyphicon-stop";
    private String color;
    private String backColor;
    private String href;
    private Boolean selectable = true;
    private List<String> tags;

    private TreeNodeState state = new TreeNodeState();
    private List<TreeNode> nodes;

    public TreeNode() {

    }

    public TreeNode(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setState(Boolean checked, Boolean disabled,Boolean selected) {
        this.state.setChecked(checked);
        this.state.setDisabled(disabled);
        this.state.setSelected(selected);
    }

    public void setState(Boolean checked, Boolean disabled, Boolean expanded, Boolean selected) {
        this.state = new TreeNodeState(checked, disabled, expanded, selected);
    }

    public void setStateChecked(Boolean checked) {
        this.state.setChecked(checked);
    }

    public void setStateDisabled(Boolean disabled) {
        this.state.setDisabled(disabled);
    }

    public void setStateExpanded(Boolean expanded) {
        this.state.setExpanded(expanded);
    }

    public void setStateSelected(Boolean selected) {
        this.state.setSelected(selected);
    }

    public void addTreeNode(TreeNode treeNode) {
        this.nodes.add(treeNode);
    }

}
