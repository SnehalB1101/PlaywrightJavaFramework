package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasiscsTest
{   Page page;
    Browser browser;
    Playwright playwright;

    @BeforeMethod
     public void setup()
{
    playwright = Playwright.create();
    browser= playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    page=browser.newPage();
    page.setDefaultTimeout(60000);
    page.navigate("https://eventhub.rahulshettyacademy.com/login");
    PlaywrightAssertions.setDefaultAssertionTimeout(7000);
}

    @Test(description= "Create Event -Book that Event and verify if its booked")
    public void DemoTest()
{
    System.out.println(page.title());
    assertThat(page).hasTitle("EventHub — Discover & Book Events");
    page.getByPlaceholder("you@email.com").fill("rahulshetty1@yahoo.com");
    page.getByLabel("Password").fill("Magiclife1!");
    page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();
    assertThat(page.getByRole(AriaRole.LINK,
            new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
    page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    page.locator("#event-title-input").fill("QA Summit Rahul Shetty",new Locator.FillOptions().setTimeout(10000));
    page.locator("#admin-event-form textarea").fill("Rahul Shetty QA Meetups");
    page.getByLabel("Category").selectOption("Concert");
    page.getByLabel("City").fill("Test City");
    page.getByLabel("Venue").fill("Test Venue");
    page.getByLabel("Event Date & Time").fill("2027-10-29T16:54");
    page.getByLabel("Price ($)").fill("100");
    page.getByLabel("Total Seats").fill("50");
    page.locator("#add-event-btn").click();
    assertThat(page.getByText("Event Created!")).isVisible();
    page.locator("#nav-events").click(new Locator.ClickOptions().setTimeout(12000));
    Locator eventCards = page.getByTestId("event-card");
    page.waitForTimeout(3000);
    System.out.println(eventCards.count());
    //page.waitForTimeout(3000);
    Locator targetCard=eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit Rahul Shetty"));
    assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
    String seatsText= targetCard.getByText("seats").innerText();
    System.out.println(seatsText);
    int seatsNumBeforeBooking=Integer.parseInt(seatsText.split(" ")[0]);
    targetCard.getByTestId("book-now-btn").click();
    page.getByLabel("Full Name").fill("Test Student");
    page.locator("#customer-email").fill("test.student@example.com");
    page.getByPlaceholder("+91 98765 43210").fill("9876543210");
    page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Confirm Booking")).click();
    assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    String bookingRef=page.locator(".booking-ref").innerText();
    page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("View My Bookings")).click();
    Locator bookingCards=page.locator("#booking-card");
    Locator targetBookingCard= bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
    assertThat(targetBookingCard).isVisible();
    page.locator("#nav-events").click();
    page.waitForTimeout(1000);
    Locator eventCardsAfterBooking = page.getByTestId("event-card");
    Locator targetCardAfterBooking=eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit Rahul Shetty"));
    String seatsTextAfterBooking= targetCard.getByText("seats").innerText();
    System.out.println(seatsTextAfterBooking);
    int seatsNumAfterBooking=Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);
    Assert.assertTrue(seatsNumBeforeBooking>seatsNumAfterBooking);








}



}
