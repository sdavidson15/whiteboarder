var routePrefix = "localhost:8080/whiteboarder"

function postSession() {
        //assert(typeof id == "string");
        var url = routePrefix + "session";
        $.ajax({
            type: "POST",
            url: url,
            data: application-json,
            success: function(data){
                // do something on success
            },
            dataType: "text"
        });

        $.post(routePrefix + "/session", "", function() {
            // upon successful session post

        });
}

$(function() {
        $("#wb-join-session").click(function() {
                postSession();
        });
});
