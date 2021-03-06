package com.github.invictum.test.pages;

import com.github.invictum.pages.AbstractPage;
import com.github.invictum.unified.data.provider.UnifiedDataProvider;
import com.github.invictum.unified.data.provider.UnifiedDataProviderFactory;
import com.github.invictum.unified.data.provider.UnifiedDataProviderUtil;
import com.github.invictum.utils.url.EnhancedPageUrls;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbstractPage.class, UnifiedDataProviderFactory.class, UnifiedDataProviderUtil.class})
public class AbstractPageTest {

    private static EnhancedPageUrls urlsMock = null;
    private WebDriver driver = null;

    @Before
    public void setupTest() throws Exception {
        urlsMock = mock(EnhancedPageUrls.class);
        whenNew(EnhancedPageUrls.class).withAnyArguments().thenReturn(urlsMock);
        mockStatic(UnifiedDataProviderFactory.class);
        mockStatic(UnifiedDataProviderUtil.class);
        UnifiedDataProvider dataProvider = new UnifiedDataProvider();
        when(UnifiedDataProviderFactory.class, "getInstance", anyObject()).thenReturn(dataProvider);
        driver = mock(WebDriver.class);
    }

    @Test
    public void toStringTest() {
        AbstractPage page = new AbstractPage();
        assertThat("To string method value is wrong.", page.toString(), equalTo("AbstractPage"));
    }

    @Test
    public void isPageUrlCompatibleTest() throws Exception {
        when(driver.getCurrentUrl()).thenReturn("http://localhost/path/session#part");
        when(urlsMock, "getPageUrlPattern").thenReturn("http://localhost/path/.+?");
        AbstractPage page = new AbstractPage(driver);
        assertThat("Page isn't compatible to current url.", page.isPageUrlCompatible(), equalTo(true));
    }

    @Test
    public void isPageUrlWithParamsCompatibleTest() throws Exception {
        when(driver.getCurrentUrl()).thenReturn("http://localhost/path/param1/path/param2/");
        when(urlsMock, "getPageUrlPattern").thenReturn("http://localhost/path/.+?/path/.+?/");
        AbstractPage page = new AbstractPage(driver);
        assertThat("Page isn't compatible to current url.", page.isPageUrlCompatible(), equalTo(true));
    }

    @Test
    public void locateAllByXpathTest() throws Exception {
        String locatorValue = "//xpath";
        when(UnifiedDataProviderUtil.class, "getLocatorByKey", anyObject(), anyObject()).thenReturn(locatorValue);
        when(driver.findElements(By.xpath(locatorValue))).thenReturn(new ArrayList<WebElement>());
        new AbstractPage(driver).locateAll("locatorKey");
        verify(driver, times(1)).findElements(By.xpath(locatorValue));
    }

    @Test
    public void locateAllByCssTest() throws Exception {
        String locatorValue = ".css";
        when(UnifiedDataProviderUtil.class, "getLocatorByKey", anyObject(), anyObject()).thenReturn(locatorValue);
        when(driver.findElements(By.cssSelector(locatorValue))).thenReturn(new ArrayList<WebElement>());
        new AbstractPage(driver).locateAll("locatorKey");
        verify(driver, times(1)).findElements(By.cssSelector(locatorValue));
    }

    @Test
    public void setPageUrlsTest() throws Exception {
        AbstractPage page = new AbstractPage();
        page.setPageUrls(urlsMock);
        Field field = page.getClass().getDeclaredField("pageUrls");
        field.setAccessible(true);
        EnhancedPageUrls actual = (EnhancedPageUrls) field.get(page);
        assertThat("Page urls weren't set.", actual, equalTo(urlsMock));
    }
}
