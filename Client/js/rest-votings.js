/*
    * Здесь все скрипты загружаются после скачивания страницы html
     */
$( document ).ready(function() {
    /*
    * Это функция для всплывающего окна "Список проголосовавших". Она делает GET-запрос по data-id
     */
    $('.ls-modal').on('click', function(e){
        e.preventDefault();
        var id = $(this).attr('data-id');
        var url0 = "/rest/votings/";
        var url1 = url0.concat(id.toString());
        $(".modal-body").html(id);
        $('#myModal').modal('show').find('.modal-body').load($(this).attr('href'));
    });
    /*
    * Это функция для выведения на экран список всех голосований. Делаем GET-запрос по Ajax
     */
    $.getJSON("/rest/votings/", function ( data, textStatus, jqXHR ) { // указываем url и функцию обратного вызова
        var maxId = data['maxId'];
        $("#main-container").html('');      //очищаем div
        for (var i=0; i<maxId; i++) {
            var url0 = "/rest/votings/";
            var stri = (i+1).toString();
            var url1 = url0.concat(stri);
            $.getJSON(url1, function (data, textStatus, jqXHR) {
                var id = data['id'];
                var title = data['title'];
                var description = data['description'];
                var options = data['options'];
                var authorWallet = data['authorWallet'];
                var begin = data['begin'];
                var end = data['end'];
                var active = data['active'];
                var panel_body_id = "panel-body-id-"+id.toString();
                $("#main-container").append('<div class="inner cover voting-container">' +
                    '<div class="col-xs-12 col-md-8">' +
                    '<div class="panel panel-default">' +
                    '<div class="panel-heading">' +
                    '<h3 class="panel-title">'+title+'</h3>' +
                    '</div>' +
                    '<div id="'+panel_body_id+'" class="panel-body">' + description +
                    '<br/>Ниже представлены варианты:<br/>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="col-xs-6 col-md-4">' +
                    '<a data-id="'+i+'" class="ls-modal" href="#">Просмотреть список проголосовавших</a><br/>' +
                    '<span style="font-size: 12px;color:red;">Голосовать могут только авторизированные пользователи</span><br/>' +
                    '</div>' +
                    '</div>');
                var votes_summa = 0;
                for (var j=0; j<options.length; j++) {
                    var votes = options[j]['votes'];
                    votes_summa += votes;
                }
                for (var j=0; j<options.length; j++) {
                    var number = options[j]['number'];
                    var name = options[j]['name'];
                    var votes = options[j]['votes'];
                    var wallets = options[j]['wallets'];
                    var percent = (votes/votes_summa*100).toFixed(0);
                    var sharp_panel_body_id = "#" + panel_body_id;
                    $(sharp_panel_body_id).append('<span style="float: left; ">'+ name +'</span><br/>' +
                        '<div class="progress">' +
                        '<div class="progress-bar" role="progressbar" aria-valuenow="'+percent+'" aria-valuemin="0" aria-valuemax="100" style="width: '+percent+'%;">' +
                        '<span class="sr-only">Complete</span>'+ votes +
                        '</div>' +
                        '</div>');
                }
            });
        }
    });


});