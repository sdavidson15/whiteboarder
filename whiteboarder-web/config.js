var config = (function () {
    var url,

        getUrl = function () {
            return url;
        },

        init = function () {
            $.ajax("runtype.txt", {
                type: 'GET',
                success: function (response) {
                    switch (response.trim()) {
                        case "prod":
                            url = 'http://proj-309-yt-c-1.cs.iastate.edu';
                            return;
                        case "local":
                            url = 'http://localhost:8080';
                            return;
                        default:
                            url = 'http://localhost:8080';
                            console.error("Unable to read run type, using local url.");
                    }
                }
            });
        }

    return {
        init: init,
        getUrl: getUrl
    };
}());