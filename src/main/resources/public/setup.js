$(document).ready(function () {
    $("#submit-button").on("click", function (e) {
        e.preventDefault();
        const configUrl = "http://localhost:8080/save-config";
        const requestData = {
            userName: $("#username").val(),
            password: $("#password").val(),
        };
        $.ajax({
            type: "POST",
            url: configUrl,
            headers: {
                "Content-Type": "application/json",
            },
            data: JSON.stringify(requestData),
            success: function () {
                clearMessages();
                const message = "The configuration has been saved and activated successfully";
                $("#config-form").append(`<div class="submit-message success"><i class="fa fa-check"></i> ${message}</div>`)
            },
            error: function (xhr, thrownError) {
                clearMessages();
                const message = "Failed to activate the configuration";
                $("#config-form").append(`<div class="submit-message failure"><i class="fa fa-times"></i> ${message}</div>`)
            }
        });
    });
});

clearMessages = () => {
    $(".submit-message").remove()
}