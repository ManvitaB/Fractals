# Fractals
Fractals is a web app for generating fractals via recursion for a set amount of iterations,
and is run on Spring Framework in Java.

# Fractal Types
Currently the only fractal that may be generated is a [2D Fractal Tree](src/main/java/com/fractals/FractalTree.java).
Each iteration *i* generates 2<sup>*i*</sup> line segment nodes.

This Fractal Tree was created with 14 iterations, 
an angle of 60 degrees between child nodes, 
and a scaling factor of 0.77, meaning each child line segment is 77% as long as its parent.

![Image of Fractal Tree](src/main/resources/static/images/example-fractal-tree.png)
