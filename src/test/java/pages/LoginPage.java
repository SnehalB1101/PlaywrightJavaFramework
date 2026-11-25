package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    Page page;
    String baseurl;
    private static final String emailPlaceholder= "you@email.com";
    private static final String password_label="Password";
    public LoginPage(Page page,String baseurl)
    {
     this.page=page;
     this.baseurl=baseurl;
    }
    public DashboardPage loginToApplication()
    {
        page.navigate(baseurl);
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder(emailPlaceholder).fill("rahulshetty1@yahoo.com");
        page.getByLabel(password_label).fill("Magiclife1!");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();
        DashboardPage dashboardPage=new DashboardPage(page);
        return dashboardPage;
    }
}
