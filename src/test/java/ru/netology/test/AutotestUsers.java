package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AutotestUsers {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Successfully login with active registered user")
    void successfullyLoginActiveRegisteredUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2")
                .shouldHave(exactText("Личный кабинет"))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Error message if login with not registered user")
    void getErrorIfLoginNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Error message if login with blocked user")
    void getErrorIfLoginBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Error message if user registered but login wrong")
    void getErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(visible);
    }

    @Test
    @DisplayName("Error message if user registered but password wrong")
    void getErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(visible);
    }
}
