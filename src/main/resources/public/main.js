$(document).ready(function () {
    $("#projects").DataTable({
        ajax: {
            url: config.projectsUrl,
            dataSrc: 'content',
            processing: true,
            error: function () {
                const message = "Failed to load projects"
                $("#table-wrapper").html(`<div class="submit-message failure"><i class="fa fa-times"></i> ${message}</div>`)
            }
        },
        columns: [
            { 'data': 'name' },
            { 'data': 'status' },
            { 'data': 'sourceLang' },
            { 'data': 'targetLangs' }
        ]
    });
});


