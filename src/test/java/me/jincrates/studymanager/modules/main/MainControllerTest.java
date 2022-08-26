package me.jincrates.studymanager.modules.main;

import me.jincrates.studymanager.infra.AbstractContainerBaseTest;
import me.jincrates.studymanager.infra.MockMvcTest;
import me.jincrates.studymanager.modules.account.AccountRepository;
import me.jincrates.studymanager.modules.account.AccountService;
import me.jincrates.studymanager.modules.account.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.mail.MessagingException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Junut5를 쓸 때는 RunWith를 쓸 필요가 없다.
@MockMvcTest
class MainControllerTest extends AbstractContainerBaseTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() throws MessagingException {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("jincrates");
        signUpForm.setEmail("jincrates@email.com");
        signUpForm.setPassword("12345678");
        accountService.processNewAccount(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 성공")
    @Test
    void loginWithEmail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "jincrates@email.com")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jincrates"));  //Username이 이메일이 아닌 이유는 UserAccount에서 닉네임을 넣었기 때문
    }

    @DisplayName("닉네임으로 로그인 성공")
    @Test
    void loginWithNickname() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "jincrates")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jincrates"));  //Username이 이메일이 아닌 이유는 UserAccount에서 닉네임을 넣었기 때문
    }

    @DisplayName("로그인 실패")
    @Test
    void loginFail() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "jincratesFail")
                        .param("password", "1234567890")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @DisplayName("로그아웃 테스트")
    @Test
    void logoutTest() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}