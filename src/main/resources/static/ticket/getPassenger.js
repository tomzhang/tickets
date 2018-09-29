$(function () {
    getPassenger();

})

function getPassenger() {

    $.ajax({
        type: "post",
        url: context+"ticket/getPassengerDTOs",
        timeout: 60 * 1000,
        success: function (data) {
            $("#passenger tbody").remove();
            var html="<tbody>";
            $(data).each(function (index,obj) {
                html +="<tr>";
                html +="<td> <input type='checkbox' name='passenger' /> &nbsp;&nbsp; "+obj.passenger_name +" </td>";
                html +="</tr>";
            })
            html +="</tbody>";
            $("#passenger").append(html);
        },
        error: function (message) {
        }
    });
}