package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;

import org.apache.commons.lang3.time.DateUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestCardDelivery {


    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        Configuration.headless = true;
    }

    SelenideElement form = $("form");
    Date date = new Date();
    SimpleDateFormat d = new SimpleDateFormat("dd.MM.YYYY");
    String dateCurrentPlusThreeDays = d.format(DateUtils.addDays(date, 3));
    String dateCurrentPlusTwoDays = d.format(DateUtils.addDays(date, 2));
    String dateCurrentPlusFourDays = d.format(DateUtils.addDays(date, 4));

    @Test
    void shouldSubmitRequest() {
        form.$("[data-test-id=city] input").setValue("Ханты-Мансийск");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitRequestIfCityContainЁ() {
        form.$("[data-test-id=city] input").setValue("Орёл");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitRequestIfCityContainHyphen() {
        form.$("[data-test-id=city] input").setValue("Ростов-на-Дону");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitRequestIfCityContainSpace() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldNotSubmitRequestIfCityIsNotAdminCenter() {
        form.$("[data-test-id=city] input").setValue("Балашиха");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $(".input_invalid").shouldHave(exactText("Доставка в выбранный город недоступна"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfCityContainLatin() {
        form.$("[data-test-id=city] input").setValue("Ufa");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfCityIsEmpty() {
        form.$("[data-test-id=city] input").setValue("");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfDateIsTwoDaysFromCurrent() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusTwoDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfDateIsFourDaysFromCurrent() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusFourDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldNotSubmitRequestIfDateIsEmpty() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue("");
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfDayInDateNotExist() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue("32.12.2100");
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfMonthInDateNotExist() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue("01.13.2100");
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfYearInDateIsTooBig() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue("01.12.9999");
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfYearInDateIsTooSmall() {
        form.$("[data-test-id=city] input").setValue("Нижний Новгород");
        form.$("[data-test-id=date] input").sendKeys(Keys.CONTROL + "a");
        form.$("[data-test-id=date] input").sendKeys(Keys.DELETE);
        form.$("[data-test-id=date] input").setValue("01.12.0000");
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameIsMoreTwoWords() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван Иванович");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameContainDigital() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван9");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameContainLatin() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Ivan");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameContainSpecialSymbol() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов_Иван");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameContainOnlyOneWord() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameIsEmpty() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameContainOnlySpaces() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("      ");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldSubmitRequestIfNameContainHyphen() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов-Задунайский Иван");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitRequestIfNameContainOnlyHyphens() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("-----");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestIfNameContainOnlyOneHyphen() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("-");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$("[data-test-id=agreement]").click();
        form.$(".button__text").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }

    @Test
    void shouldNotSubmitRequestByNotIncludedCheckbox() {
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue(dateCurrentPlusThreeDays);
        form.$("[data-test-id=name] input").setValue("Иванов Иван");
        form.$("[data-test-id=phone] input").setValue("+79033223322");
        form.$(".button__text").click();
        $("[data-test-id='agreement'].input_invalid").shouldBe(Condition.visible);
        $("[data-test-id=notification]").shouldNotBe(Condition.visible);
    }


}
