function queryTrains() {

    var fromStation= $("#from_station").attr("alt");
    var fromStation_code=$("#from_station").attr("data-id");
    var toStation= $("#to_station").attr("alt");
    var toStation_code=$("#to_station").attr("data-id");
    var train_date = $("#train_date").val();

    $.ajax({
        type: "post",
        url: context+"ticket/queryTrains",
        timeout: 60 * 1000,
        data: {
         fromStation:fromStation,
         fromStation_code:fromStation_code,
         toStation:toStation,
         toStation_code:toStation_code,
         train_date:train_date
        },
        success: function (data) {
            var trainLineInfos =data.trainLineInfos;
            $("#ticket_table tbody").remove();
            var html="<tbody>";
            $(trainLineInfos).each(function (index,obj) {
               // alert(obj.from_station_y_name);
                html +="<tr>";
                html +="<td> "+obj.station_train_code +" </td>";
                html +="<td> "+obj.from_station_y_name +" </td>";
                html +="<td> "+obj.to_station_y_name +" </td>";
                html +="<td> "+obj.duration +" </td>";
                html +="<td> "+obj.swz_num +" </td>";
                html +="<td> "+obj.zy_num +" </td>";
                html +="<td> "+obj.ze_num +" </td>";
                html +="<td> "+obj.gjrw_num +" </td>";
                html +="<td> "+obj.rw_num +" </td>";
                html +="<td> "+obj.dw_num +" </td>";
                html +="<td> "+obj.yw_num +" </td>";
                html +="<td> "+obj.rz_num +" </td>";
                html +="<td> "+obj.yz_num +" </td>";
                html +="<td> "+obj.wz_num +" </td>";
                html +="<td> "+obj.qt_num +" </td>";
                html +="<td> "+obj.start_time +" </td>";

                html +="</tr>";
            })
            html +="</tbody>";
            $("#ticket_table").append(html);
        },
        error: function (message) {
        }
    });


}