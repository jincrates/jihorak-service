package me.jincrates.studymanager.modules.temp;

import lombok.RequiredArgsConstructor;
import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.account.CurrentAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TempDataController {

    private final TempDateService tempDateService;

    @GetMapping("/temp/data-account")
    public String generateTempAccounts(@CurrentAccount Account account) {
        tempDateService.generateTempAccounts();
        return "redirect:/";
    }

    @GetMapping("/temp/data-study")
    public String generateTempStudies(@CurrentAccount Account account) {
        tempDateService.generateTempStudies(account);
        return "redirect:/";
    }
}
