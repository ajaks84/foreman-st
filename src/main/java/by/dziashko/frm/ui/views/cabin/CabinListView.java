package by.dziashko.frm.ui.views.cabin;

import by.dziashko.frm.backend.entity.aspirator.*;
import by.dziashko.frm.backend.entity.cabin.CabinData;
import by.dziashko.frm.backend.service.cabin.CabinDataService;
import by.dziashko.frm.ui.forms.cabin.CabinDataForm;
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

@Route(value = "cabins", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class CabinListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<CabinData> grid = new Grid<>(CabinData.class);
    CabinDataForm form;

    CabinDataService cabinDataService;

    public CabinListView(CabinDataService cabinDataService) {
        this.cabinDataService = cabinDataService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Cabin_List"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new CabinDataForm();
        form.addListener(CabinDataForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(CabinDataForm.CloseEvent.class, e -> closeEditor());
        Div content = new Div(grid);
        content.addClassName("content");
        content.setHeightFull();
        content.setWidth("85%");
        Div content_2 = new Div( form);
        HorizontalLayout layoutTop = new HorizontalLayout(content,content_2);
        layoutTop.setSizeFull();
        add(getToolbar(), layoutTop);

        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {

        Button addContactButton = new Button(getTranslation("New_Aspirator"));
        addContactButton.addClickListener(click -> addCabin());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("seller-grid");
        grid.setSizeFull();
        grid.setColumns("modelName");
        grid.getColumnByKey("modelName").setHeader(getTranslation("modelName"));
        grid.addColumn(orderName -> {
            AspiratorBody aspiratorBody = orderName.getAspiratorBody();
            return aspiratorBody == null ? "-" : aspiratorBody.getModelName();
        }).setHeader(getTranslation("aspiratorBody")).setSortable(true);
        grid.addColumn(orderName -> {
            AspiratorFan aspiratorFan = orderName.getAspiratorFan();
            return aspiratorFan == null ? "-" : aspiratorFan.getModelName();
        }).setHeader(getTranslation("aspiratorFan")).setSortable(true);
        grid.addColumn(orderName -> {
            AspiratorElectric aspiratorElectric = orderName.getAspiratorElectric();
            return aspiratorElectric == null ? "-" : aspiratorElectric.getModelName();
        }).setHeader(getTranslation("aspiratorElectric")).setSortable(true);
        grid.addColumn(orderName -> {
            AspiratorOption aspiratorOption = orderName.getAspiratorOption();
            return aspiratorOption == null ? "-" : aspiratorOption.getModelName();
        }).setHeader(getTranslation("aspiratorOption")).setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(false));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    void addCabin() {
        grid.asSingleSelect().clear();
        editCabinData(new AspiratorData());
    }

    private void updateList() {
        grid.setItems(cabinDataService.findAll());
    }

    public void editCabinData(CabinData cabinData) {
        if (cabinData == null) {
            closeEditor();
        } else {
            form.setCabinData(cabinData);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorData aspiratorData) {
        if (aspiratorData == null) {
            LOGGER.info("Can't navigate to aspirator");
        } else {
            String s = aspiratorData.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("aspirator-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setCabinData(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(CabinDataForm.SaveEvent event) {
        cabinDataService.save(event.getAspiratorData());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}

