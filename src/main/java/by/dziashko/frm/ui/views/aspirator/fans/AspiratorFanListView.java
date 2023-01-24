package by.dziashko.frm.ui.views.aspirator.fans;

import by.dziashko.frm.backend.entity.aspirator.AspiratorFan;
import by.dziashko.frm.backend.service.aspirator.AspiratorFanService;
import by.dziashko.frm.ui.forms.aspirator.AspiratorFanForm;
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

@Route(value = "fans", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class AspiratorFanListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<AspiratorFan> grid = new Grid<>(AspiratorFan.class);
    AspiratorFanForm form;

    AspiratorFanService aspiratorDataService;

    public AspiratorFanListView(AspiratorFanService aspiratorDataService) {
        this.aspiratorDataService = aspiratorDataService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("aspiratorFanList"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new AspiratorFanForm();
        form.addListener(AspiratorFanForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(AspiratorFanForm.CloseEvent.class, e -> closeEditor());
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

        Button addContactButton = new Button(getTranslation("New_aspiratorFan"));
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
//        grid.getColumnByKey("bodyModel").setHeader(getTranslation("bodyModel"));
//        grid.getColumnByKey("fanMotorData").setHeader(getTranslation("fanMotorData"));
//        grid.getColumnByKey("electricBoxVersion").setHeader(getTranslation("electricBoxVersion"));
//        grid.getColumnByKey("optionList").setHeader(getTranslation("optionList"));
        grid.getColumns().forEach(col -> col.setAutoWidth(false));
        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    void addAspirator() {
        grid.asSingleSelect().clear();
        editFanMotorData(new AspiratorFan());
    }

    private void updateList() {
        grid.setItems(aspiratorDataService.findAll());
    }

    public void editFanMotorData(AspiratorFan aspiratorFan) {
        if (aspiratorFan == null) {
            closeEditor();
        } else {
            form.setFanMotorData(aspiratorFan);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorFan aspiratorFan) {
        if (aspiratorFan == null) {
            LOGGER.info("Can't navigate to aspirator");
        } else {
            String s = aspiratorFan.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("aspirator-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setFanMotorData(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(AspiratorFanForm.SaveEvent event) {
        aspiratorDataService.save(event.getFanMotorData());
        updateList();
        closeEditor();
    }

    private void deleteAspiratorData(AspiratorFanForm.DeleteEvent event) {
        aspiratorDataService.delete(event.getFanMotorData());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }}
