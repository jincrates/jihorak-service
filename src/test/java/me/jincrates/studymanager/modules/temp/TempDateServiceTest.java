package me.jincrates.studymanager.modules.temp;

import me.jincrates.studymanager.modules.account.Account;
import me.jincrates.studymanager.modules.account.AccountRepository;
import me.jincrates.studymanager.modules.tag.Tag;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TempDateServiceTest {

    @Autowired TempDateService tempDateService;
    @Autowired AccountRepository accountRepository;

    @Disabled @Test
    @DisplayName("사용자 임시 데이터 생성")
    void generateTempAccounts() {
        tempDateService.generateTempAccounts();
        List<Account> accounts = accountRepository.findAll();
        System.out.println(accounts);
    } 
    
}