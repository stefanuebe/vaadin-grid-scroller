/*
 * Copyright 2021 Stefan Uebe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.addons.stefan;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.vaadin.addons.stefan.gridscroller.GridScroller;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class View extends Div {

    public View() {
        Grid<String> grid = new Grid<>();
        for (int i = 0; i < 25; i++) {
            grid.addColumn(String::toString).setHeader("Header").setWidth("200px");
        }

        List<Grid.Column<String>> columns = grid.getColumns();
        HeaderRow headerRow = grid.appendHeaderRow();

        // some example headers
        for (Grid.Column<String> column : columns) {
            ComboBox<String> filter = new ComboBox<>("", "A", "B", "C");
            filter.setWidth("0");

            HorizontalLayout layout = new HorizontalLayout(filter);
            layout.setPadding(false);
            layout.setMargin(false);
            layout.setFlexGrow(1, filter);

            headerRow.getCell(column).setComponent(layout);
        }

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            items.add("Value " + i);
        }

        grid.setItems(items);

        GridScroller gridScroller = new GridScroller(grid);

        NumberField numberField = new NumberField("", "Position in pixels");
        numberField.setValueChangeMode(ValueChangeMode.EAGER);
        numberField.setId("input");
        numberField.setWidth("200px");

        Button buttonTop = new Button("Set top", event -> {
            gridScroller.scrollTop(numberField.getValue());
        });
        buttonTop.setId("top");

        Button buttonLeft = new Button("Set left", event -> {
            gridScroller.scrollLeft(numberField.getValue());
        });
        buttonLeft.setId("left");

        Button buttonBoth = new Button("Set both", event -> {
            Double value = numberField.getValue();
            gridScroller.scroll(value, value);
        });
        buttonBoth.setId("both");

        Span result = new Span("0.0,0.0");
        result.setId("result");
        Div resultWrapper = new Div(new Span("Left,Top (in pixels): "), result);

        Button buttonRead = new Button("Read pos", event -> {
            gridScroller.readScrollPositions(scrollPosition -> result.setText(scrollPosition.getLeft() + "," + scrollPosition.getTop()));
        });
        buttonRead.setId("read");

        add(new HorizontalLayout(numberField, buttonTop, buttonLeft, buttonBoth, buttonRead, resultWrapper), grid);
    }
}
