package by.dziashko.frm.ui.views.aspirator.bodys;

import by.dziashko.frm.backend.entity.aspirator.AspiratorBody;
import by.dziashko.frm.backend.service.aspirator.AspiratorBodyService;
import by.dziashko.frm.ui.forms.aspirator.AspiratorBodyForm;
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

@Route(value = "bodys", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class AspiratorBodyListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<AspiratorBody> grid = new Grid<>(AspiratorBody.class);
    AspiratorBodyForm form;

    AspiratorBodyService aspiratorBodyService;

    public AspiratorBodyListView(AspiratorBodyService aspiratorBodyService) {
        this.aspiratorBodyService = aspiratorBodyService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("aspiratorBodyList"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new AspiratorBodyForm();
        form.addListener(AspiratorBodyForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(AspiratorBodyForm.CloseEvent.class, e -> closeEditor());
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

        Button addContactButton = new Button(getTranslation("New_aspiratorBody"));
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
        grid.getColumns().forEach(col -> col.setAutoWidth(false));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    void addAspirator() {
        grid.asSingleSelect().clear();
        editEntry(new AspiratorBody());
    }

    private void updateList() {
        grid.setItems(aspiratorBodyService.findAll());
    }

    public void editEntry(AspiratorBody aspiratorBody) {
        if (aspiratorBody == null) {
            closeEditor();
        } else {
            form.setEditEntry(aspiratorBody);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorBody aspiratorBody) {
        if (aspiratorBody == null) {
            LOGGER.info("Can't navigate to aspirator");
        } else {
            String s = aspiratorBody.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("aspirator-body-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setEditEntry(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(AspiratorBodyForm.SaveEvent event) {
        aspiratorBodyService.save(event.getEditEntry());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }}
