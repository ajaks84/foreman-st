package by.dziashko.frm.ui.views.cyclone;

import by.dziashko.frm.backend.entity.aspirator.AspiratorData;
import by.dziashko.frm.backend.entity.cabin.CabinData;
import by.dziashko.frm.backend.entity.cyclone.CycloneData;
import by.dziashko.frm.backend.service.cyclone.CycloneDataService;
import by.dziashko.frm.ui.forms.cabin.CabinDataForm;
import by.dziashko.frm.ui.forms.cyclone.CycloneDataForm;
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

@Route(value = "cyclones", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class CycloneListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<CycloneData> grid = new Grid<>(CycloneData.class);
    CycloneDataForm form;

    CycloneDataService cycloneDataService;

    public CycloneListView(CycloneDataService cycloneDataService) {
        this.cycloneDataService = cycloneDataService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("Cyclone_List"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new CycloneDataForm();
        form.addListener(CycloneDataForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(CycloneDataForm.CloseEvent.class, e -> closeEditor());
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

        Button addCabinButton = new Button(getTranslation("New_Cyclone"));
        addCabinButton.addClickListener(click -> addCabin());

        HorizontalLayout toolbar = new HorizontalLayout(addCabinButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("seller-grid");
        grid.setSizeFull();
        grid.setColumns("modelName");
        grid.getColumnByKey("modelName").setHeader(getTranslation("modelName"));
        grid.getColumns().forEach(col -> col.setAutoWidth(false));
    }

    void addCabin() {
        grid.asSingleSelect().clear();
        editCabinData(new CycloneData());
    }

    private void updateList() {
        grid.setItems(cycloneDataService.findAll());
    }

    public void editCabinData(CycloneData cycloneData) {
        if (cycloneData == null) {
            closeEditor();
        } else {
            form.setCycloneData(cycloneData);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorData cabinData) {
        if (cabinData == null) {
            LOGGER.info("Can't navigate to CycloneData");
        } else {
            String s = cabinData.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("cyclone-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setCycloneData(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(CycloneDataForm.SaveEvent event) {
        cycloneDataService.save(event.getCycloneData());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
    }

}

