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
package org.vaadin.addons.stefan.gridscroller;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonArray;

import java.util.Objects;

/**
 * Utility class that provides methods to set and read the pixel based scroll position of a Vaadin grid.
 * @author Stefan Uebe
 */
public class GridScroller {
    private final Grid<?> grid;

    /**
     * Creates a new instance for the given grid.
     *
     * @param grid grid to manage
     * @throws NullPointerException when given parameter is null
     */
    public GridScroller(Grid<?> grid) {
        this.grid = Objects.requireNonNull(grid, "Grid is required");
    }

    /**
     * Sets the top / vertical / y scroll position in pixels for the given grid. Passing {@code null} resets
     * the scroll position to the top.
     *
     * @param grid  grid to modify
     * @param value new scroll position
     */
    public static void scrollTop(Grid<?> grid, Double value) {
        modifyScrollValue(grid, value, "scrollTop");
    }

    /**
     * Sets the left / horizontal / x scroll position in pixels for the given grid. Passing {@code null} resets
     * the scroll position to the left.
     *
     * @param grid  grid to modify
     * @param value new scroll position
     */
    public static void scrollLeft(Grid<?> grid, Double value) {
        modifyScrollValue(grid, value, "scrollLeft");
    }

    /**
     * Executes the property update on the internal table element. There is no check, if the given scrollProperty
     * exists or is valid, so assure yourself to not pass harmful code.
     *
     * @param grid           grid to modify
     * @param value          value to set
     * @param scrollProperty property to be changed
     * @return pending java script result
     */
    @SuppressWarnings("UnusedReturnValue")
    private static PendingJavaScriptResult modifyScrollValue(Grid<?> grid, Double value, String scrollProperty) {
        return grid.getElement().executeJs("this.$.table[$1] = $0;", value != null ? value : 0, scrollProperty);
    }

    /**
     * Sets the both scroll positions in pixels for the given grid. Passing {@code null} resets
     * the respective scroll position.
     *
     * @param grid  grid to modify
     * @param left new scroll position for left / horizontal / x
     * @param top new scroll position for top / vertical / y
     */
    public static void scroll(Grid<?> grid, Double left, Double top) {
        grid.getElement().executeJs("this.$.table.scrollLeft = $0; this.$.table.scrollTop = $1;",
                left != null ? left : 0,
                top != null ? top : 0);
    }

    /**
     * Sets the both scroll positions in pixels for the given grid. Passing {@code null} resets
     * the scroll positions.
     *
     * @param grid  grid to modify
     * @param scrollPosition new scroll position
     */
    public static void scroll(Grid<?> grid, ScrollPosition scrollPosition) {
        if (scrollPosition == null) {
            scroll(grid, null, null);
        } else {
            scroll(grid, scrollPosition.getLeft(), scrollPosition.getTop());
        }
    }


    /**
     * Sets the both scroll positions in pixels for the grid. Passing {@code null} resets
     * the respective scroll position.
     *
     * @param left new scroll position for left / horizontal / x
     * @param top new scroll position for top / vertical / y
     */
    public void scroll(Double left, Double top) {
        scroll(getGrid(), left, top);
    }

    /**
     * Sets the both scroll positions in pixels for the grid. Passing {@code null} resets
     * the scroll positions.
     *
     * @param scrollPosition new scroll position
     */
    public void scroll(ScrollPosition scrollPosition) {
        scroll(getGrid(), scrollPosition);
    }

    /**
     * Reads the current scroll position of the given grid and passes it to the given consumer. Please notify,
     * that this will do a round trip to the client, calling the consumer afterwards with the client result.
     * If the client cannot be reached, then the consumer will not be called.
     *
     * @param grid            grid to read from
     * @param leftTopConsumer consumer for the scroll position
     * @throws IllegalStateException return value from the client is not a json array
     */
    public static void readScrollPositions(Grid<?> grid, SerializableConsumer<ScrollPosition> leftTopConsumer) {
        grid.getElement()
                .executeJs("return [this.$.table.scrollLeft, this.$.table.scrollTop]")
                .then(jsonValue -> {
                    if (jsonValue instanceof JsonArray) {
                        double left = ((JsonArray) jsonValue).getNumber(0);
                        double top = ((JsonArray) jsonValue).getNumber(1);
                        leftTopConsumer.accept(new ScrollPosition(left, top));
                    } else {
                        throw new IllegalStateException("Could not read from client, result is not a json array, but " + jsonValue.asString());
                    }
                });
    }

    /**
     * Sets the top / vertical / y scroll position in pixels for the grid. Passing {@code null} resets
     * the scroll position to the top.
     *
     * @param value new scroll position
     */
    public void scrollTop(Double value) {
        scrollTop(getGrid(), value);
    }

    /**
     * Sets the left / horizontal / x scroll position in pixels for grid. Passing {@code null} resets
     * the scroll position to the left.
     *
     * @param value new scroll position
     */
    public void scrollLeft(Double value) {
        scrollLeft(getGrid(), value);
    }

    /**
     * Reads the current scroll position of given grid and passes it to the given consumer. Please notify,
     * that this will do a round trip to the client, calling the consumer afterwards with the client result.
     * If the client cannot be reached, then the consumer will not be called.
     *
     * @param leftTopConsumer consumer for the scroll position
     * @throws IllegalStateException return value from the client is not a json array
     */
    public void readScrollPositions(SerializableConsumer<ScrollPosition> leftTopConsumer) {
        readScrollPositions(getGrid(), leftTopConsumer);
    }

    /**
     * Returns the wrapped grid.
     * @return grid
     */
    public Grid<?> getGrid() {
        return grid;
    }

    /**
     * Container class providing information about scroll positions in a two dimensional component.
     */
    public static class ScrollPosition {

        private final double left;
        private final double top;

        public ScrollPosition(double left, double top) {
            this.left = left;
            this.top = top;
        }

        /**
         * Returns the left / horizontal / x scroll position in pixels (starting from the left border of the
         * scrollable component).
         *
         * @return left scroll position
         */
        public double getLeft() {
            return left;
        }

        /**
         * Returns the top / vertical / y scroll position in pixels (starting from the top border of the
         * scrollable component).
         *
         * @return top scroll position
         */
        public double getTop() {
            return top;
        }
    }
}
