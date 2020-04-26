/**
 jsgrid extension
 author: tx
 */

// custom field: boolean
let BooleanField = function(config) {
    jsGrid.Field.call(this, config);
};

BooleanField.prototype = new jsGrid.Field({
    align: "center",
    autosearch: true,
    readOnly: false,

    sorter: function (bool1, bool2) {
        return (bool1 ? 1 : 0) - (bool2 ? 1 : 0);
    },

    itemTemplate: function (value) {
        return !!value
            ? "<small class=\"badge badge-danger\">禁用</small>"
            : "<small class=\"badge badge-success\">启用</small>";
    },

    insertTemplate: function (value) {
        return this._insertPicker = $("<input type='checkbox'>").prop("checked", true);
    },

    editTemplate: function (value) {
        return this._editPicker = $("<input type='checkbox'>").prop("checked", value);
    },

    insertValue: function () {
        return this._insertPicker.prop("checked");
    },

    editValue: function () {
        return this._editPicker.prop("checked");
    }
});

jsGrid.fields.boolean = BooleanField;