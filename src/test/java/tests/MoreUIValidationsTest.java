package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationsTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));


        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @AfterMethod()
    public void tearDown() {
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }


    @Test
    public void ChildWindowHandle()
    {
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(() -> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
        page.getByLabel("Username:").fill(emailId);
        System.out.println(page.getByLabel("Username:").inputValue());
        page.waitForTimeout(3000);
    }
        @Test(groups={"smoke"})
        public void UIControls()
        {
            Locator UserRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
            UserRdBtn.click();

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
            Assert.assertTrue(UserRdBtn.isChecked());

            Locator checkBoxTerms=page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
            checkBoxTerms.check();
            Assert.assertTrue(checkBoxTerms.isChecked());

            page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
            page.waitForTimeout(3000);
        }

    }




