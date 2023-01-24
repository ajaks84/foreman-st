package by.dziashko.frm.ui.views.sellers;

import by.dziashko.frm.backend.entity.productionOrder.Seller;
import by.dziashko.frm.backend.service.productionOrder.SellerService;
import by.dziashko.frm.security.SecuredByRole;
import by.dziashko.frm.ui.forms.productionOrder.SellerListForm;
import by.dziashko.frm.ui.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@SecuredByRole("ROLE_Admin")
@Route(value = "sellers", layout = MainView.class)
@CssImport("./styles/views/sellers/sellers-list-view.css")
public class SellersListView extends VerticalLayout implements LocaleChangeObserver {

    SellerService sellerService;
    Grid<Seller> grid = new Grid<>(Seller.class);
    SellerListForm form;

    public SellersListView(SellerService sellerService) {
        this.sellerService = sellerService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Sellers_List"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new SellerListForm();
        form.addListener(SellerListForm.SaveEvent.class, this::saveSeller);
        form.addListener(SellerListForm.CloseEvent.class, e -> closeEditor());
        Div content = new Div(grid);
        content.addClassName("content");
        content.setHeightFull();
        content.setWidth("75%");
        Div content_2 = new Div( form);
        HorizontalLayout layoutTop = new HorizontalLayout(content,content_2);
        layoutTop.setSizeFull();
        add(getToolbar(), layoutTop);

        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {

        Button addContactButton = new Button(getTranslation("New_Seller"));
        addContactButton.addClickListener(click -> addSeller());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    void addSeller() {
        grid.asSingleSelect().clear();
        editSeller(new Seller());
    }

    private void configureGrid() {
        grid.addClassName("seller-grid");
        grid.setSizeFull();
        grid.setColumns("name", "lastName", "phoneNumber");
        grid.getColumnByKey("name").setHeader(getTranslation("Name"));
        grid.getColumnByKey("lastName").setHeader(getTranslation("LastName"));
        grid.getColumnByKey("phoneNumber").setHeader(getTranslation("phoneNumber"));
        grid.getColumns().forEach(col -> col.setAutoWidth(false));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    private void updateList() {
        grid.setItems(sellerService.findAll());
    }

    public void editSeller(Seller seller) {
        if (seller == null) {
            closeEditor();
        } else {
            form.setSeller(seller);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(Seller seller) {
        if (seller == null) {
            LOGGER.info("Can't navigate to seller");
        } else {
            String s = seller.getName();
            grid.getUI().ifPresent(ui -> ui.navigate("seller-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setSeller(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveSeller(SellerListForm.SaveEvent event) {
        sellerService.save(event.getSeller());
        updateList();
        closeEditor();
    }

    private void deleteSeller(SellerListForm.DeleteEvent event) {
        sellerService.delete(event.getSeller());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
