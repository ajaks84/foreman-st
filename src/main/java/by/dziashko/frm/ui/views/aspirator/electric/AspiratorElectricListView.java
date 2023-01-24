package by.dziashko.frm.ui.views.aspirator.electric;

import by.dziashko.frm.backend.entity.aspirator.AspiratorBody;
import by.dziashko.frm.backend.entity.aspirator.AspiratorElectric;
import by.dziashko.frm.backend.service.aspirator.AspiratorElectricService;
import by.dziashko.frm.ui.forms.aspirator.AspiratorBodyForm;
import by.dziashko.frm.ui.forms.aspirator.AspiratorElectricForm;
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

@Route(value = "aspirator-electric", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class AspiratorElectricListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<AspiratorElectric> grid = new Grid<>(AspiratorElectric.class);
    AspiratorElectricForm form;

    AspiratorElectricService aspiratorElectricService;

    public AspiratorElectricListView(AspiratorElectricService aspiratorElectricService) {
        this.aspiratorElectricService = aspiratorElectricService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("aspiratorElectricList"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new AspiratorElectricForm();
        form.addListener(AspiratorElectricForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(AspiratorElectricForm.CloseEvent.class, e -> closeEditor());
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

        Button addContactButton = new Button(getTranslation("New_aspiratorElectric"));
        addContactButton.addClickListener(click -> addListEntry());

        HorizontalLayout toolbar = new HorizontalLayout(addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("seller-grid");
        grid.setSizeFull();
        grid.setColumns("modelName");
        grid.getColumnByKey("modelName").setHeader(getTranslation("modelName"));
        grid.getColumns().forEach(col -> col.setAutoWidth(false));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    void addListEntry() {
        grid.asSingleSelect().clear();
        editEntry(new AspiratorElectric());
    }

    private void updateList() {
        grid.setItems(aspiratorElectricService.findAll());
    }

    public void editEntry(AspiratorElectric aspiratorElectric) {
        if (aspiratorElectric == null) {
            closeEditor();
        } else {
            form.setEditEntry(aspiratorElectric);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorElectric aspiratorElectric) {
        if (aspiratorElectric == null) {
            LOGGER.info("Can't navigate to entry");
        } else {
            String s = aspiratorElectric.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("aspirator-electric-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setEditEntry(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(AspiratorElectricForm.SaveEvent event) {
        aspiratorElectricService.save(event.getEditEntry());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }}
