# Grid Scroller

A utility addon that provides a simply api to read and set the scroll position. The utility
can be used with a wrapped grid or via static methods.

## Usage
### Setting scroll positions
```
Grid<?> grid = new Grid<>();
// ... init grid

// specify the scroll values, e.g. from a previous view state
double topScroll = 100.0;
double leftScroll = 37.0;

// Set the scroll positions

GridScroller gridScroller = new GridScroller(grid);
gridScroller.scrollTop(topScroll);
gridScroller.scrollLeft(leftScroll);

// Alternatively you can call

gridScroller.scroll(leftScroll, topScroll);

// If you do not want to wrap the grid, use the static API

GridScroller.scrollTop(grid, topScroll);

// ...
```
### Read the current scroll positions
```
Grid<?> grid = new Grid<>();
// ... init grid

GridScroller gridScroller = new GridScroller(grid);

// readPosition reads the position from the client side and passes it to a callback

gridScroller.readScrollPositions(scrollPosition -> 
    result.setText(scrollPosition.getLeft() + "," + scrollPosition.getTop()))
    
// If you do not want to wrap the grid, use the static API

GridScroller.readScrollPositions(grid, scrollPosition -> 
    result.setText(scrollPosition.getLeft() + "," + scrollPosition.getTop()))
```

## Development instructions
Starting the test/demo server:
1. Run `mvn jetty:run`.
2. Open http://localhost:8080 in the browser.
