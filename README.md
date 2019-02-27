# Fractals
Fractals is a web app for generating fractals via recursion for a set amount of iterations,
and is run on 
[Spring Framework](https://spring.io/)
in Java.

## Fractal Types
Currently the user may generate and play around with a 
[2D Fractal Tree](src/main/java/com/fractals/FractalTree.java)
or [2D Fractal Circles](src/main/java/com/fractals/FractalCircle.java).

### Fractal Tree
A [FractalTree](src/main/java/com/fractals/FractalTree.java)
branches from a parent line segment into two child segments each iteration that change in scale
depending on the scaling factor.
Each iteration *i* generates 2<sup>*i*</sup> line segment nodes.

This Fractal Tree was created with 14 iterations, 
an angle of 60 degrees between child nodes, 
and a scaling factor of 0.77, meaning each child line segment is 77% as long as its parent.

![Image of Fractal Tree](src/main/resources/static/examples/example-fractal-tree.png)
### Fractal Circles
[FractalCircle](src/main/java/com/fractals/FractalCircle.java)
generates a set number of child circle 'satellites' each iteration that intersect with the parent circle at a single point.
Every iteration satellite radius changes proportionally to the scaling factor.
With *s* satellites, each iteration *i* generates *s*<sup>*i*</sup> circle satellites.

These Fractal Circles were generated with only 4 iterations, 10 satellites, and a scaling factor of 0.5,
meaning each satellite has half the radius of its parent and thus a quarter of the area.

![Image of Fractal Circles](src/main/resources/static/examples/example-fractal-circle.png)
