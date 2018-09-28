$(function () {
        $("#from_station").bsSuggest({
            url:context+"12306/station_data.json",
            effectiveFieldsAlias:{serial:"序号",station_name: "车站名",station_code:"车站代码",station_py:"车站拼音检索"},
            ignorecase: true,
            showHeader: true,
            showBtn: false,     //不显示下拉按钮
            delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
            idField: "station_code",
            keyField: "station_name",
            clearable: false
        }).on('onDataRequestSuccess', function (e, result) {
            console.log('onDataRequestSuccess: ', result);
        }).on('onSetSelectValue', function (e, keyword, data) {
            console.log('onSetSelectValue: ', keyword, data);
        }).on('onUnsetSelectValue', function () {
            console.log("onUnsetSelectValue");
        });

        $("#to_station").bsSuggest({
            url:context+"12306/station_data.json",
            effectiveFieldsAlias:{serial:"序号",station_name: "车站名",station_code:"车站代码",station_py:"车站拼音检索"},
            ignorecase: true,
            showHeader: true,
            showBtn: false,     //不显示下拉按钮
            delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
            idField: "station_code",
            keyField: "station_name",
            clearable: false
        }).on('onDataRequestSuccess', function (e, result) {
            console.log('onDataRequestSuccess: ', result);
        }).on('onSetSelectValue', function (e, keyword, data) {
            console.log('onSetSelectValue: ', keyword, data);
        }).on('onUnsetSelectValue', function () {
            console.log("onUnsetSelectValue");
        });

});



