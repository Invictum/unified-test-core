package com.github.invictum.steps;

import com.github.invictum.pages.AbstractPage;
import com.github.invictum.utils.ResourceProvider;
import com.github.invictum.utils.properties.PropertiesUtil;
import net.serenitybdd.core.SerenitySystemProperties;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.pages.Pages;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.Set;

import static com.github.invictum.utils.properties.EnhancedSystemProperty.PagesPackageName;

/**
 * Utils steps class allows to open pages in more easy way.
 */
public class PageNavigationSteps extends AbstractSteps {

    public static final String PAGES_PACKAGE = PropertiesUtil.getProperty(PagesPackageName);
    public static final String PAGE_SUFFIX = "Page";
    public static final Logger LOG = LoggerFactory.getLogger(PageNavigationSteps.class);
    public static final int TIMEOUT = SerenitySystemProperties.getProperties()
            .getIntegerValue(ThucydidesSystemProperty.WEBDRIVER_TIMEOUTS_IMPLICITLYWAIT, 30000);

    public PageNavigationSteps() {
        super();
        checkPackage();
    }

    public PageNavigationSteps(Pages pages) {
        super(pages);
        checkPackage();
    }

    /**
     * Method opens page by its string name representation.
     * Mind the page construction logic, it adds PAGE_SUFFIX to given parameter. (e. g. Home became Home + Suffix).
     *
     * @param pageName String
     */
    @Step
    public void openPage(String pageName) {
        AbstractPage pageToOpen = getPageByName(pageName);
        open(pageToOpen);
    }

    /**
     * Method opens page by its class.
     *
     * @param pageClass Class
     */
    @Step
    public void openPage(Class<? extends AbstractPage> pageClass) {
        AbstractPage page = pages().getPage(pageClass);
        open(page);
    }

    /**
     * Method opens page by its string name representation and additional parameters.
     * Mind the page construction logic, it adds PAGE_SUFFIX to given parameter. (e. g. Home became Home + Suffix).
     *
     * @param pageName String
     * @param params String[]
     */
    @Step
    public void openPageWithParams(String pageName, String... params) {
        AbstractPage pageToOpen = getPageByName(pageName);
        open(pageToOpen, params);
    }

    /**
     * Method opens page by its class with additional parameters.
     *
     * @param pageClass Class
     * @param params String[]
     */
    @Step
    public void openPageWithParams(Class<? extends AbstractPage> pageClass, String... params) {
        AbstractPage page = pages().getPage(pageClass);
        open(page, params);
    }

    private void open(AbstractPage page, String... parameters) {
        page.setImplicitTimeout(TIMEOUT, ChronoUnit.MILLIS);
        page.open(parameters);
        page.resetImplicitTimeout();
    }

    private AbstractPage getPageByName(String pageName) {
        String fullPageName = String.format("%s%s", pageName, PAGE_SUFFIX);
        Reflections reflections = new Reflections(ClasspathHelper.forPackage(PAGES_PACKAGE));
        Set<Class<? extends AbstractPage>> availableClasses = reflections.getSubTypesOf(AbstractPage.class);
        for (Class<? extends AbstractPage> pageClass : availableClasses) {
            if (StringUtils.equals(pageClass.getSimpleName(), fullPageName)) {
                return pages().getPage(pageClass);
            }
        }
        throw new IllegalStateException(String.format("%s page is not found", pageName));
    }

    private void checkPackage() {
        if (StringUtils.equals(PAGES_PACKAGE, PagesPackageName.defaultValue())) {
            LOG.warn("Project root is used as pages package. You may redefine it with '{}' property", PagesPackageName);
        } else if (!ResourceProvider.isPackagePresent(PAGES_PACKAGE)) {
            LOG.error("Configure pages package with '{}' property", PagesPackageName);
        }
    }
}
