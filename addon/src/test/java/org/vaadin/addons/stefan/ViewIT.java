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

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.textfield.testbench.NumberFieldElement;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.addons.stefan.gridscroller.GridScroller.ScrollPosition;

public class ViewIT extends AbstractViewTest {

    @Test
    public void test_readScroll_initial() {
        getReadScrollButton().click();
        assertValidPosition(0, 0);
    }

    @Test
    public void test_readScroll_fromUtilsScroll_topOnly() {
        getInput().setValue("100");
        getTopScrollButton().click();

        getReadScrollButton().click();
        assertValidPosition(0, 100);
    }

    @Test
    public void test_readScroll_fromUtilsScroll_leftOnly() {
        getInput().setValue("100");
        getLeftScrollButton().click();

        getReadScrollButton().click();
        assertValidPosition(100, 0);
    }

    @Test
    public void test_readScroll_fromUtilsScroll() {
        getInput().setValue("50");
        getLeftScrollButton().click();

        getInput().setValue("100");
        getTopScrollButton().click();

        getReadScrollButton().click();
        assertValidPosition(50, 100);

        getInput().setValue("37");
        getBothScrollButton().click();

        getReadScrollButton().click();
        assertValidPosition(37, 37);
    }


    /**
     * Scrolling by pixels might lead to a slight different result due to double conversion and internal pixel management
     * of the browser. Therefore we check for a specific range in which the resulting scroll position should lay in.
     * Automatically reads the value from the result span.
     *
     * @param left expected left scroll position
     * @param top expected top scroll position
     */
    private void assertValidPosition(double left, double top) {
        assertValidPosition(left, top, parseResult());
    }

    private ScrollPosition parseResult() {
        String text = getResult().getText();
        String[] split = text.split(",");
        return new ScrollPosition(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }

    private void assertValidPosition(double expectedLeft, double expectedTop, ScrollPosition positionToTest) {
        assertInRange(expectedLeft, positionToTest.getLeft(), "left");
        assertInRange(expectedTop, positionToTest.getTop(), "top");
    }

    private void assertInRange(double expectedValue, double valueToTest, String property) {
        double lower = expectedValue - 1;
        double upper = expectedValue + 1;
        Assert.assertTrue("Expected " + property + " to be between " + lower + " and " + upper
                        + ", but it was " + valueToTest,
                lower < valueToTest && valueToTest < upper);
    }


    private GridElement getGrid() {
        return $(GridElement.class).first();
    }

    private ButtonElement getLeftScrollButton() {
        return $(ButtonElement.class).id("left");
    }

    private ButtonElement getTopScrollButton() {
        return $(ButtonElement.class).id("top");
    }
    private ButtonElement getBothScrollButton() {
        return $(ButtonElement.class).id("both");
    }

    private ButtonElement getReadScrollButton() {
        return $(ButtonElement.class).id("read");
    }

    private NumberFieldElement getInput() {
        return $(NumberFieldElement.class).id("input");
    }

    private SpanElement getResult() {
        return $(SpanElement.class).id("result");
    }
}
