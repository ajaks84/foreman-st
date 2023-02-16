package by.dziashko.frm.ui.views.addOptions;

import by.dziashko.frm.backend.entity.addOptions.AdditionalOptions;
import by.dziashko.frm.backend.entity.aspirator.AspiratorData;
import by.dziashko.frm.backend.service.addOptions.AdditionalOptionsService;
import by.dziashko.frm.ui.forms.addOptions.AdditionalOptionsForm;
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

@Route(value = "add-options", layout = MainView.class)
@CssImport("./styles/views/common/common-view.css")
public class AdditionalOptionsListView extends VerticalLayout implements LocaleChangeObserver {

    Grid<AdditionalOptions> grid = new Grid<>(AdditionalOptions.class);
    AdditionalOptionsForm form;

    AdditionalOptionsService additionalOptionsService;

    public AdditionalOptionsListView(AdditionalOptionsService additionalOptionsService) {
        this.additionalOptionsService = additionalOptionsService;

        UI current = UI.getCurrent();
        current.getPage().setTitle(getTranslation("addOptionList"));

        addClassName("list-view");
        setSizeFull();
        getToolbar();
        configureGrid();

        form = new AdditionalOptionsForm();
        form.addListener(AdditionalOptionsForm.SaveEvent.class, this::saveAspiratorData);
        form.addListener(AdditionalOptionsForm.CloseEvent.class, e -> closeEditor());
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

        Button addCabinButton = new Button(getTranslation("New_Option"));
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
//        grid.addItemClickListener(event -> navigateTo(event.getItem()));
    }

    void addCabin() {
        grid.asSingleSelect().clear();
        editCabinData(new AdditionalOptions());
    }

    private void updateList() {
        grid.setItems(additionalOptionsService.findAll());
    }

    public void editCabinData(AdditionalOptions additionalOptions) {
        if (additionalOptions == null) {
            closeEditor();
        } else {
            form.setAdditionalOptions(additionalOptions);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void navigateTo(AspiratorData cabinData) {
        if (cabinData == null) {
            LOGGER.info("Can't navigate to cabin");
        } else {
            String s = cabinData.getModelName();
            grid.getUI().ifPresent(ui -> ui.navigate("cabin-details" + "/" + s));
        }
    }

    private void closeEditor() {
        form.setAdditionalOptions(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveAspiratorData(AdditionalOptionsForm.SaveEvent event) {
        additionalOptionsService.save(event.getAdditionalOptions());
        updateList();
        closeEditor();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
    }

}

