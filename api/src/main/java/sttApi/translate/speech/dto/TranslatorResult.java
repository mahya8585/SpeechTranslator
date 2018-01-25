package sttApi.translate.speech.dto;

/**
 * 翻訳APIから返却される情報のdto
 * Java9を見据えてLombokは使ってません
 */
public class TranslatorResult {
    private String type;
    private String id;
    private String recognition;
    private String translation;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
