TODO List
=========

Demo Editor
-----------

### Bugs
 
 1. Column width is not adjusted properly when the max node depth is changed. 
 1. Double / float fields stay with edited background color even after editing complete. 


### Refactorings

 1. Move interpolators to flowutils library. 
 1. Create interpolator functions.
     1. Add parameters for the interpolators where they are relevant
     1. Create a separate Range type for Interpolators.
     1. The aim is to unify interpolators and normal functions if possible.  
 1. Maybe remove quad, cubic, quart and quint interpolators, or rename to pow2, pow3, pow4, pow5. 
 1. Add a calculator that takes a function and applies it to a parameter
 1. Change noise to use Vector2:s for offsets
 1. Change sine and noise to use min and max output value instead of amplitude and offset
 1. Separate functions with two parameters and fields into two different Range objects, as they are conceptually different, although same in practice
 1. Rename Range to Type or similar.
 
 
### Features   

 1. Add save and load of demo as an xml file.
 1. Main time view bar for demo, with current visible area (drag to pan, wheel to zoom), showing currently edited time and current demo progress
 1. Select activation and deactivation times for effects (with sliders on time view)
 1. Implement gradient editing with values at time positions and interpolators for interpolating between them.
 1. Add support for adding and removing variables (dynamically created parameters).
    Add button shown in context menu.
 1. Add context menu to node UIs, with cut, copy and paste (maybe have a paste buffer with last 4-8 entries or so).
    Also have paste as new variable.
 1. Add support for adding and removing effects.
    Both with buttons and from context menu.
 1. Add support for exporting and importing individual effects or calculators as libraries.
 1. Programmable Pixel shader, converts calculators to shader code?? (only supports some variable and calculator types - double, vector2, vector3, color, int, etc.)
 1. Expression function / calculator, that parses an expression and generates calculator / function / field based on that (compile to java bytecode, or use in shader).
 1. Add undo/redo support if feasible

  
### Content  
  
 1. Add all basic common 1, 2 and 3 etc double => double math operations. (add, sub, mul, div, mod, round, floor, wrap, clamp, mix, map, ceil, min, max, pow, log, exp, fuzzy logic, etc)  
 1. Add comparison operations (1 or 2 doubles => boolean).  
 1. Add logic operations (1 or 2 booleans => boolean).  
 1. Add if calculator / function.  
 1. Add 3D noise, implementing field and providing the third dimension as an additional parameter, to allow easily animated noise.
 1. Add 1D noise.
 1. Add 2D noise implementing function, and providing additional dimension as parameter, for animated 1D noise.
 1. Add oscillator function based on oscillator function code from soundrasp project (supported large variety of waveforms IIRC).
 1. Add color gradient / function type
 1. Implement and add Worley noise (1D, 2D, 3D versions, with lots of customizable parameters). 
 1. Particle system effect     
 1. Star scroller effect? (Non physical particle system?)     
 1. Triangle heightfield effect.
     * Line graphics or filled triangles.
     * A number of ecotypes/textures for each corner, blend between them based on weight.
 1. Grid based liquid simulation.
 1. Item distribution on heightfield.
 1. Whole screen shader effect (e.g. atmosphere, etc). 
    Load shader code from file, or multiline text field.  
 1. Recursive shape generation, for tree and other shapes.  (Skeletal, possibly animated?)
     * Use calculators to generate the shapes - add some kind of shape types.
 1. Genetic algorithm for evolving controllers for articulated skeletons / particle systems / shapes.   
 1. Real-time sound generation, based on soundrasp code?      
 
 
 