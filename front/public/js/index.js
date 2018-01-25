$(function() {
    //最新メッセージを表示(メッセージ直下の要素へ移動)
    function rolldown() {
        setTimeout(function() {
            window.scroll(0, $(document).height());
        }, 0);
    }

    var socket = io.connect();

    //emitで送信されたデータを処理する
    socket.on("msg", function(data, fn) {
        var msgp = document.createElement("p");
        msgp.innerHTML = data.msg;
        $("#messageArea").append(msgp);
    });

    var serverDomain = "https://xxxxxxxxxxxx.azurewebsites.net";

    //音声ファイル送信
    $("button#sendFile").click(function() {
        // フォームデータを取得
        let formData = new FormData($("#dataForm").get(0));
        let url = serverDomain + "/stt";
        console.log(url);

        // POSTでアップロード
        $.ajax({
                url: url,
                type: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                dataType: "html"
            })
            .done(function(data, textStatus, jqXHR) {
                console.log("post message");
                $("#messageFile").val("");

                rolldown();
            })
            .fail(function(jqXHR, textStatus, errorThrown) {
                console.log("fail");
            });
    });

    //テキストpublish
    $("#publisher").click(function() {
        let message = $("#message").val();
        socket.emit("publisher", message);
        $("#message").val("");

        rolldown();
    });

    //議事録出力
    $("#close").click(function() {
        let outputMessages = [];
        $("#messageArea p").each(function() {
            outputMessages.push($(this).text());
        })

        let createLogFileUrl = serverDomain + "/output";
        $.ajax({
                url: createLogFileUrl,
                type: "POST",
                data: JSON.stringify(outputMessages),
                contentType: "application/json"
            })
            .done(function(data, textStatus, jqXHR) {
                console.log("create log file");
                socket.emit("publisher", data);
                rolldown();
            })
            .fail(function(jqXHR, textStatus, errorThrown) {
                console.log("fail");
            });
    });


    var isMeeting = false;

    //ボタンのテキストが 会議開始の時 録音を開始する
    //ボタンのテキストが 会議終了の時 録音を終了する
    //TODO トグルでいい感じにしたい
    $("#meeting").click(function() {

        if ($("#meeting").text() == "会議開始") {

            isMeeting = true;
            $("#meeting").text("会議終了");
            startMeeting(5);

        } else {

            $("#meeting").text("会議開始");
            isMeeting = false;
        }
    });

    //音声議事録を開始する
    //second秒毎に録音した内容を翻訳APIに送信する
    function startMeeting(second) {

        //録音内容を削除する
        clearRecording();
        //録音を開始する
        startRecording();

        setTimeout(() => {

            //録音を停止する
            stopRecording();

            //録音された音声ファイルを取得する
            getWav(function(wav) {

                //isMeeting = false の時録音を終了する
                if (!isMeeting) {
                    return;
                }

                //再帰的に処理実行する
                startMeeting(second);

                let url = serverDomain + "/stt";

                //音声ファイルを非同期に送信する
                sendWav(wav, url).then(
                    (data, textStatus, jqXHR) => {
                        console.log("create log file");
                        socket.emit("publisher", data);
                        rolldown();
                    },
                    (jqXHR, textStatus, errorThrown) => {
                        console.log("fail");
                    }
                )

            });

        }, second * 1000);
    }

    window.onload = function() {

        //マイクの使用許可等、録音の準備を行う
        initRecoder();
    };
});