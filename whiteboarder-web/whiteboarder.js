
var routePrefix = "/api/wb"

function getSession(id) {
        assert(typeof id == "string");
        $.get(routePrefix + "/session/" + id);
}

$(function() {
        $("#wb-join-session").click(function() {
                getSession();
        });
});
