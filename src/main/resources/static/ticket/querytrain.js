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
            alert(data);
        },
        error: function (message) {
        }
    });


}