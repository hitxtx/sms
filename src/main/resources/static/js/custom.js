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

    jQuery.prototype.serializeObject = function () {
        let a, o, h, i, e;
        a = this.serializeArray();
        o = {};
        h = o.hasOwnProperty;
        for (i = 0; i < a.length; i++) {
            e = a[i];
            if (!h.call(o, e.name)) {
                o[e.name] = e.value;
            }
        }
        return o;
    }

    jQuery.extend({
        doAjax: function (method, url, data) {
            return new Promise((resolve, reject) => {
                // 1.创建XHR
                const xhr = new XMLHttpRequest();
                xhr.responseType = 'json';
                // 装配参数
                let params = formatParams(data);
                if (method === 'GET' || method === 'get') {
                    url += '?' + params;
                }
                // 2.初始化
                xhr.open(method, url);
                // 3.发送请求
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.send(method === 'POST' || method === 'post' ? params : null);
                // 4.绑定事件
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4) {
                        if (xhr.status === 200) {
                            if (xhr.responseURL.endsWith("/login")) {
                                window.location.href = xhr.responseURL;
                            }
                            let res = xhr.response;
                            if (!res.success) {
                                toastr.warning(res.message);
                            }
                            resolve(res);
                        } else if (xhr.status === 302) {
                            window.location.href = xhr.responseURL;
                        } else {
                            let res = xhr.response;
                            toastr.warning(res.message);
                            reject(res);
                        }
                    }
                }
            })
        }
    });

    function formatParams(data) {
        let params = "";

        if (data instanceof Array) {
            for (let d of data) {
                params += d.name + '=' + d.value + '&';
            }
            params = params.slice(0, params.length - 1);
        }

        if (data instanceof Object) {
            let pp = data.hasOwnProperty;
            for (let d in data) {
                params += d + '=' + data[d] + '&';
            }
            params = params.slice(0, params.length - 1);
        }

        return params;
    }
});
