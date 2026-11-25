package tests;

import com.microsoft.playwright.Locator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class FrameworkBuildDataDrivenTest extends TestBase{

    @DataProvider(name="eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJasonDataToMap("/src/test/resources/eventBookingData.json");

    }

    @Test(groups={"Framework"},dataProvider = "eventBookingData",description= "Create Event -Book that Event and verify if its booked")
    public void DemoTest(HashMap<String,String>data)
{

    String eventTitle= "Playwright Framework Test";
    LoginPage loginPage=new LoginPage(page,baseurl);
    loginPage.loginToApplication();
    DashboardPage dashboardPage=loginPage.loginToApplication();
    dashboardPage.waitForEventsToLoad();
    AdminEventsPage adminEventsPage=new AdminEventsPage(page);
    adminEventsPage.goTo();
    adminEventsPage.createEvent
            (
            data.get("titlePrefix"),
            data.get("description"),
            data.get("city"),
                    data.get("venue"),
            data.get("dateTime"),
            data.get("price"),
            data.get("totalSeats")
    );



    EventsPage eventsPage =new EventsPage(page);
    eventsPage.goTo();
    Locator targetCard=eventsPage.findEventCard(data.get("titlePrefix"));
    int seatsNumBeforeBooking= eventsPage.getSeatsCount(targetCard);
    BookingFormPage bookingFormPage=eventsPage.ProceedToBookingEvent(targetCard);
    bookingFormPage.fillAndConfirm(
            data.get("fullName"),
            data.get("email"),
            data.get("phone")
    );

    System.out.println("URL before Full Name = " + page.url());
    //page.getByLabel("Full Name").fill("Test Student");
    //page.locator("#customer-email").fill("test.student@example.com");
    //page.getByPlaceholder("+91 98765 43210").fill("9876543210");
    //page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Confirm Booking")).click();
    //assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    //String bookingRef=page.locator(".booking-ref").innerText();
    //page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("View My Bookings")).click();
    //Locator bookingCards=page.locator("#booking-card");
    //Locator targetBookingCard= bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
    //assertThat(targetBookingCard).isVisible();
    //page.locator("#nav-events").click();
    //page.waitForTimeout(1000);
    //Locator eventCardsAfterBooking = page.getByTestId("event-card");
    //Locator targetCardAfterBooking=eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit Rahul Shetty"));

    //String seatsTextAfterBooking= targetCard.getByText("seats").innerText();
    //System.out.println(seatsTextAfterBooking);
    //int seatsNumAfterBooking=Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);
    //Assert.assertTrue(seatsNumBeforeBooking>seatsNumAfterBooking);








}



}
