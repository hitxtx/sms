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

    jQuery.extend({
        doHTTP: function (method, url, data, callback) {
            let xhr;
            if (window.XMLHttpRequest) {//IE7+, Firefox, Chrome, Opera, Safari
                xhr = new XMLHttpRequest();
            } else {// code for IE6, IE5
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xhr.onreadystatechange = function () {
                if (xhr.status === 200 && xhr.readyState === 4) {
                    let res = JSON.parse(xhr.responseText);
                    if (!res.success) {
                        toastr.warning(res.message);
                    } else {
                        // 调用回调函数,并将响应数据传入回调函数
                        callback(res);
                    }
                }
                if (xhr.status === 302 || xhr.status === 0) {
                    window.location.href = xhr.responseURL;
                }
                if (xhr.status === 500) {
                    toastr.error('系统异常！');
                }
            };

            let params = '';
            //这里使用stringify方法将js对象格式化为json字符串
            if (data instanceof Array) {
                for (let d of data) {
                    params += d.name + '=' + d.value + '&';
                }
                // 使用slice函数提取一部分字符串，这里主要是为了去除拼接的最后一个&字符
                // slice函数：返回一个新的字符串。包括字符串从 start 开始（包括 start）到 end 结束（不包括 end）为止的所有字符。
                params = params.slice(0, params.length - 1);
            }
            if (data instanceof Object) {
                for (let d in data) {
                    params += d + '=' + data[d] + '&';
                }
                params = params.slice(0, params.length - 1);
            }
            // 判断method是否为get
            if (method === "get" || method === "GET") {
                // 是则将数据拼接在url后面
                url = '?' + params;
            }
            // 初始化请求
            xhr.open(method, url, true);

            // 如果method == post
            if (method === "post" || method === "POST") {
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                // 发送请求
                xhr.send(params);
            } else {
                // 发送请求
                xhr.send(null);
            }
        }
    });

});
