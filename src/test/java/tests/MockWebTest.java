package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {


    Page page;
    Browser browser;
    Playwright playwright;

    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.setDefaultTimeout(10000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }

    @Test(description = "sandbox banner is shown when 6 events are returned")
    public void DemoTest() {
        page.getByPlaceholder("you@email.com").fill("rahulshetty1@yahoo.com");
        page.getByLabel("Password").fill("Magiclife1!");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();


        //Step 1: Create Event from Admin Page
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events6.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();


        page.waitForTimeout(7000);

        Assert.assertEquals(eventCards.count(), 6);
        //Assert.assertTrue(page.locator(".mx-1").isVisible());
        assertThat(page.locator(".mx-1").first()).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events4.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCards1 = page.getByTestId("event-card");
        assertThat(eventCards1.first()).isVisible();


        page.waitForTimeout(7000);

        Assert.assertEquals(eventCards1.count(), 4);
        //Assert.assertTrue(page.locator(".mx-1").isVisible());
        assertThat(page.locator(".mx-1").first()).isHidden();
    }

      @Test
        public void routeResumeTest()
        {

            //route.resume
            page.getByPlaceholder("you@email.com").fill("rahulshetty1@yahoo.com");
            page.getByLabel("Password").fill("Magiclife1!");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            assertThat(page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

            //Bookings

            page.getByTestId("nav-bookings").click();
            page.route("**api/bookings**", route -> route.resume(
                    new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/72116")
            ));

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
            assertThat(page.getByText("Booking not found")).isVisible();

        }


    }

