# Vaadin Grid Scroller

A utility addon that provides a simply api to read and set the scroll position. The utility
can be used with a wrapped grid or via static methods.

## Usage
### Wrapping a grid
```
Grid<?> grid = new Grid<>();
// ... init grid

// specify the scroll values, e.g. from a previous view state
double topScroll = 100.0;
double leftScroll = 37.0;

GridScroller gridScroller = new GridScroller(grid);
gridScroller.scrollTop(topScroll);
gridScroller.scrollLeft(leftScroll);



```


## Development instructions
Starting the test/demo server:
1. Run `mvn jetty:run`.
2. Open http://localhost:8080 in the browser.
