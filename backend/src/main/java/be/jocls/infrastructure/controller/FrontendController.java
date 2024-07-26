package be.jocls.infrastructure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/{path:^(?!api|static|css|js|img|fonts).*$}/**")
    public String forwardToFrontend() {
        return "forward:/index.html";
    }
}
