package sttApi.meeting;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class OutputFileCreator {
    /**
     * Storageに送付する議事録ファイルの作成を行う
     *
     * @param messages 議事内容
     * @return
     */
    public String createMeetingFile(String[] messages, String meetingDate) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(meetingTemplateResolver());

        Context context = new Context();
        //議事日付
        context.setVariable("meetingDate", meetingDate);
        //議事内容
        context.setVariable("meetingLogs", Arrays.asList(messages));

        return templateEngine.process("OutputMeeting", context);
    }

    /**
     * 議事録メッセージ用
     * template resolver
     *
     * @return
     */
    private ClassLoaderTemplateResolver meetingTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**
     * 本日日付(MTG日付)の取得
     *
     * @return
     */
    public String createMeetingDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
        LocalDateTime today = LocalDateTime.now();

        return today.format(formatter);
    }

}
