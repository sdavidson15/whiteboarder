
var routePrefix = "/whiteboarder";

$(function() {
    $("#wb-join-session").click(function() {
        var url = routePrefix + "/session";
        $.ajax({
            url: url,
            type: "POST",
            data: "",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data) {
                alert(data);
            }
        });
    });
});

