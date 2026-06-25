package com.example.loginapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormularzUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test_czyElementyStartoweSaWidoczne() {
        composeTestRule.onNodeWithText("Panel Logowania").assertIsDisplayed()
        composeTestRule.onNodeWithText("E-mail").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hasło").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zaloguj").assertIsDisplayed()
    }

    @Test
    fun test_poprawneLogowanie_wyswietlaKomunikatSukcesu() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("student@uczelnia.pl")
        composeTestRule.onNodeWithTag("password_input").performTextInput("Tajnie123!")
        composeTestRule.onNodeWithTag("login_button").performClick()
        composeTestRule.onNodeWithTag("success_message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Zalogowano pomyślnie").assertIsDisplayed()
    }

    @Test
    fun test_blednyFormatEmail_blokujePrzycisk_i_wyswietlaBlad() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("bledny_email_at_domain.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("123")
        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun test_rageClickingStabilnoscInterfejsu() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("user@test.pl")
        composeTestRule.onNodeWithTag("password_input").performTextInput("Haslo123")
        repeat(10) {
            composeTestRule.onNodeWithTag("login_button").performClick()
        }
        composeTestRule.onNodeWithTag("success_message").assertIsDisplayed()
    }

    @Test
    fun test_rotacjaEkranu_zachowujeStanFormularza() {
        val wpisanyEmail = "test_rotacji@wp.pl"
        composeTestRule.onNodeWithTag("email_input").performTextInput(wpisanyEmail)

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationLeft()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("email_input")
            .assertTextContains(wpisanyEmail, substring = true)

        device.setOrientationNatural()
        composeTestRule.waitForIdle()
    }

    @Test
    fun test_haslo5Znakow_powinnoByc_zablokowane() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("test@test.pl")
        composeTestRule.onNodeWithTag("password_input").performTextInput("Ab123") // 5 знаков
        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun test_haslo6Znakow_powinnoByc_przepuszczone() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("test@test.pl")
        composeTestRule.onNodeWithTag("password_input").performTextInput("Ab1234") // 6 знаков
        composeTestRule.onNodeWithTag("login_button").assertIsEnabled()
    }
}