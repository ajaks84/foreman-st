package by.dziashko.frm.ui.views.aspirator.aspirators;

import by.dziashko.frm.backend.entity.aspirator.*;
import by.dziashko.frm.backend.service.aspirator.*;
import by.dziashko.frm.ui.forms.aspirator.AspiratorDataForm;
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

@Route(value = "aspirators", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class AspiratorListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<AspiratorData> grid = new Grid<>(AspiratorData.class);
    AspiratorDataForm form;

    AspiratorDataService aspiratorDataService;
    AspiratorFanService aspiratorFanService;
    AspiratorBodyService aspiratorBodyService;
    AspiratorElectricService aspiratorElectricService;
    AspiratorOptionService aspiratorOptionService;

    public AspiratorListView(AspiratorFanService aspiratorFanService, AspiratorDataService aspiratorDataService,
                             AspiratorBodyService aspiratorBodyService, AspiratorElectricService aspiratorElectricService,
                             AspiratorOptionService aspiratorOptionService) {
        this.aspiratorFanService = aspiratorFanService;
        this.aspiratorDataService = aspiratorDataService;
        this.aspiratorBodyService = aspiratorBodyService;
        this.aspiratorElectricService = aspiratorElectricService;
        this.aspiratorOptionService = aspiratorOptionService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Aspirator_List"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new AspiratorDataForm(aspiratorFanService.findAll(), aspiratorBodyService.findAll(), aspiratorElectricService.findAll(), aspiratorOptionService.findAll());
        form.addListener(AspiratorDataForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(AspiratorDataForm.CloseEvent.class, e -> closeEditor());
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
        addContactButton.addClickListener(click -> addAspirator());

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

    void addAspirator() {
        grid.asSingleSelect().clear();
        editAspiratorData(new AspiratorData());
    }

    private void updateList() {
        grid.setItems(aspiratorDataService.findAll());
    }

    public void editAspiratorData(AspiratorData aspiratorData) {
        if (aspiratorData == null) {
            closeEditor();
        } else {
            form.setAspiratorData(aspiratorData);
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
        form.setAspiratorData(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(AspiratorDataForm.SaveEvent event) {
        aspiratorDataService.save(event.getAspiratorData());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}

