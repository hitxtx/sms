$(function () {
    /**
     * Form表单域重置，增加select2/input[type="hidden"]/清空
     */
    $.fn.reset = function () {
        let _form = this;
        _form[0].reset();
        _form.children('input[type="hidden"]').val("");
        let select2 = _form.find("select.select2");
        if (select2.length > 0) {
            select2.val("").trigger("change");
        }
    };
});