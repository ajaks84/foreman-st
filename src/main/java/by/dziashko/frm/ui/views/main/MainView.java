package by.dziashko.frm.ui.views.main;

import by.dziashko.frm.ui.views.aspirator.aspirators.AspiratorListView;
import by.dziashko.frm.ui.views.aspirator.bodys.AspiratorBodyListView;
import by.dziashko.frm.ui.views.aspirator.electric.AspiratorElectricListView;
import by.dziashko.frm.ui.views.aspirator.fans.AspiratorFanListView;
import by.dziashko.frm.ui.views.aspirator.option.AspiratorOptionListView;
import by.dziashko.frm.ui.views.new_orders.NewProductionOrderView;
import by.dziashko.frm.ui.views.orders.ProductionOrderView;
import by.dziashko.frm.ui.views.report.ReportView;
import by.dziashko.frm.ui.views.sellers.SellersListView;
import by.dziashko.frm.ui.views.service.ServiceView;
import by.dziashko.frm.util.RandomNumberGenerator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@PWA(name="foreman", shortName="foreman",enableInstallPrompt=false,offlineResources={"./styles/offline.css","./images/offline.png"})
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/views/main/main-view.css")
public class MainView extends AppLayout implements LocaleChangeObserver {

    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        Text text = new Text(username);
//        layout.add(new Image("images/user.svg", "Avatar"));
        int number = new RandomNumberGenerator().getRandomOneToThree();
        layout.add(new Image("images/user_"+number+".png", "Avatar"));
        Tab tab1 = new Tab("");
        tab1.setEnabled(false);
        layout.add(text,tab1);
        H1 logo = new H1("Foreman");
        logo.addClassName("logo");
        Anchor logout = new Anchor("logout", getTranslation("logout"));
        layout.addClassName("logo");
        layout.add(logout);
        Tab tab = new Tab("");
        tab.setEnabled(false);
        layout.add(tab);

        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "foreman logo"));
        logoLayout.add(new H1(getTranslation("foreman")));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        RouterLink[] links = new RouterLink[]{
                new RouterLink(getTranslation("Sellers_List"), SellersListView.class),
                new RouterLink(getTranslation("Orders_old"), ProductionOrderView.class),
                new RouterLink(getTranslation("Orders_new"), NewProductionOrderView.class),
                new RouterLink(getTranslation("Report"), ReportView.class),
                new RouterLink(getTranslation("Aspirator_List"), AspiratorListView.class),
                new RouterLink(getTranslation("aspiratorBodyList"), AspiratorBodyListView.class),
                new RouterLink(getTranslation("aspiratorFanList"), AspiratorFanListView.class),
                new RouterLink(getTranslation("aspiratorElectricList"), AspiratorElectricListView.class),
                new RouterLink(getTranslation("aspiratorOptionList"), AspiratorOptionListView.class),
                new RouterLink(getTranslation("Service"), ServiceView.class)
        };
        return Arrays.stream(links).map(MainView::createTab).toArray(Tab[]::new);
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        updateChrome();
    }

    private void updateChrome() {
        getTabWithCurrentRoute().ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabWithCurrentRoute() {
        String currentRoute = RouteConfiguration.forSessionScope()
                .getUrl(getContent().getClass());
        return menu.getChildren().filter(tab -> hasLink(tab, currentRoute))
                .findFirst().map(Tab.class::cast);
    }

    private boolean hasLink(Component tab, String currentRoute) {
        return tab.getChildren().filter(RouterLink.class::isInstance)
                .map(RouterLink.class::cast).map(RouterLink::getHref)
                .anyMatch(currentRoute::equals);
    }

    private String getCurrentPageTitle() {
        return UI.getCurrent().getInternals().getTitle();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
    }
}
