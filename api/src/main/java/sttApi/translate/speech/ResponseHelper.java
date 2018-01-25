package sttApi.translate.speech;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sttApi.translate.speech.dto.TranslatorResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseHelper {
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 翻訳結果を整形し、テキストに変換する
     * @param targetJson String型JSON翻訳結果
     * @return
     */
    public String createResponse(String targetJson) {
        final ObjectMapper mapper = new ObjectMapper();
        List<TranslatorResult> translatorResults = new ArrayList<>();

        try {
            translatorResults.add(mapper.readValue(targetJson, TranslatorResult.class));
        } catch (IOException e) {
            log.error("translate response parse ERROR!");
        }

        return translatorResults.stream()
                .map(result -> result.getTranslation())
                .collect(Collectors.joining("\n"));
    }
}
