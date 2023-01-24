package by.dziashko.frm.ui.views.aspirator.option;

import by.dziashko.frm.backend.entity.aspirator.AspiratorOption;
import by.dziashko.frm.backend.service.aspirator.AspiratorElectricService;
import by.dziashko.frm.backend.service.aspirator.AspiratorOptionService;
import by.dziashko.frm.ui.forms.aspirator.AspiratorElectricForm;
import by.dziashko.frm.ui.forms.aspirator.AspiratorOptionForm;
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

@Route(value = "aspirator-option", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class AspiratorOptionListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<AspiratorOption> grid = new Grid<>(AspiratorOption.class);
    AspiratorOptionForm form;

    AspiratorOptionService aspiratorOptionService;

    public AspiratorOptionListView(AspiratorOptionService aspiratorOptionService) {
        this.aspiratorOptionService = aspiratorOptionService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("aspiratorOptionList"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new AspiratorOptionForm();
        form.addListener(AspiratorOptionForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(AspiratorOptionForm.CloseEvent.class, e -> closeEditor());
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

        Button addContactButton = new Button(getTranslation("New_aspiratorOption"));
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
        editEntry(new AspiratorOption());
    }

    private void updateList() {
        grid.setItems(aspiratorOptionService.findAll());
    }

    public void editEntry(AspiratorOption aspiratorOption) {
        if (aspiratorOption == null) {
            closeEditor();
        } else {
            form.setEditEntry(aspiratorOption);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorOption aspiratorOption) {
        if (aspiratorOption == null) {
            LOGGER.info("Can't navigate to entry");
        } else {
            String s = aspiratorOption.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("aspirator-option-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setEditEntry(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(AspiratorOptionForm.SaveEvent event) {
        aspiratorOptionService.save(event.getEditEntry());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }}