document.getElementById("create_session_btn").addEventListener("click", function () {
        postSession();
});

function postSession() {
        $.ajax(config.getUrl() + '/whiteboarder/session', {
                type: 'POST',
                contentType: "application/json",
                success: function (response) {
                        // make cookie
                        document.cookie = "wb_session_id=" + response + ";path=/";
                        window.location.href = "GenerateQR.html"
                },
                dataType: 'text'
        });
}