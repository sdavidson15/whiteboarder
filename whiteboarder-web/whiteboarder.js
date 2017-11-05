
var routePrefix = "/whiteboarder";
var sessionID = null;

$(function() {
    var qrcode = new QRCode(document.getElementById("qrcode"), {
            width: 100,
            height: 100
    });
    var id = getCookie("wb_session_id");

    // parse id from cookie string
    function getCookie(cookieName) {
            var name = cookieName + "=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                    var c = ca[i];
                    while (c.charAt(0) == ' ') {
                            c = c.substring(1);
                    }
                    if (c.indexOf(name) == 0) {
                            return c.substring(name.length, c.length);
                    }
            }
            return "";
    }

    qrcode.makeCode(id);

    $("#create_session_btn").click(function() {
        $.ajax('http://localhost:8080/whiteboarder/session', {
            type: 'POST',
            contentType: "application/json",
            success: function (response) {
                console.log(response);
                // make cookie
                document.cookie = "wb_session_id=" + response + ";path=/";
            },
            dataType: 'text'
        });
    });
});

