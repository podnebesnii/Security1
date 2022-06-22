$('document').ready(function () {

    $('.table .btn-primary').on('click', function (event) {


        event.preventDefault();

        var href = $(this).attr('href');

        $.get(href, function (user, status) {
            $('#id').val(user.id);
            $('#name').val(user.name);
            $('#lastname').val(user.lastname);
            $('#age').val(user.age);
            $('#email').val(user.email);
            $('#password').val(user.password);

        });


        $('#editModal').modal();


    });
});