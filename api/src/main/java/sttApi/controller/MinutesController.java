package sttApi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sttApi.meeting.OutputFileCreator;
import sttApi.storage.Crud;
import sttApi.translate.speech.RequestService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 議事録処理系API
 * Created by ishida.m
 */
@RestController
public class MinutesController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String CONTAINER = "meeting";

    @Autowired
    RequestService requestService;
    @Autowired
    Crud crud;
    @Autowired
    OutputFileCreator outputFileCreator;

    /**
     * ファイルを受け取ったらメッセージを返す
     *
     * @param fileData バイナリ系ファイル
     * @return レスポンスメッセージ
     */
    @PostMapping(path = "/stt", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    void executeStt(@RequestParam MultipartFile fileData) {
        String fileName = fileData.getOriginalFilename();

        try {
            requestService.translate(fileData.getBytes());
        } catch (IOException e) {
            log.error("MultiPartFile IO Error!");
        }

        log.info("publish end");

    }

    /**
     * 議事をファイル化する
     *
     * @param messages メッセージ
     * @return ファイルURL
     */
    @PostMapping(path = "/output", produces = MediaType.APPLICATION_JSON_VALUE)
    String outputMessages(@RequestBody String[] messages) {
        //受け取った文字列をhtml化
        String meetingDateTime = outputFileCreator.createMeetingDateTime();

        try {
            //formで受け取ったfileをBLOBへ
            byte[] uploadBytes = outputFileCreator.createMeetingFile(messages, meetingDateTime).getBytes("utf-8");

            crud.upload(new ByteArrayInputStream(uploadBytes), CONTAINER,
                    meetingDateTime + ".html", uploadBytes.length);
        } catch (Exception e) {
            log.error("Storage File Upload Error!");
            return "errorが発生しました";
        }

        return meetingDateTime + ".html を保存しました。";
    }

}
