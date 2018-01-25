package sttApi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ページ遷移処理
 */
@Controller
public class PageController {
    /**
     * 接続確認ページの表示
     *
     * @return テンプレートHTML
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewIndex() {
        return "index";
    }
}
