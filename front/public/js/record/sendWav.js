
function sendWav(wav,url) {

    return new Promise((onSuccess, onFailed) => {

        let formData = new FormData();
        formData.append("fileData", wav, "voice.wav");

        $.ajax({
            url: url,
            type: "POST",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            dataType: "html"
        })
            .done(onSuccess)
            .fail(onFailed);
    });

}