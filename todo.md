TODO List
=========

Demo Editor
-----------

### Bugs
 
 1. [ ] UI is very slow for some reason.  Investigate.  Maybe listeners triggering lots of re-layouts, or similar?
     * UI sometimes freezes up while creating the editors for a demo (layout dimensions getting a nullpointer).
     * Time bars 'wobble' a bit when resizing an effect so that it de-activates.   
     * UI lags a lot and there is high CPU utilization after the demo has ended (something is busy looping, or generating a lot of updates?).
     * **It seems like this might be caused by miglayout**  Added task to write simpler layout managers and replace miglayout use. 
 1. [x] There's some issue with output scaling where a min output of 1 seems to actually translate to zero.
 1. [ ] Column width is not adjusted properly when the max node depth is changed. 
 1. [x] Double / float fields stay with edited background color even after editing complete.
 1. [ ] After the demo has stopped (or before it has started) it is not possible to delete effects, as the deletion happens in the update call where we have an opengl context and can use dispose.
    Maybe solve by having a separate manage call to effects from the opengl render call to handle initialization, setup, deletion, and maybe activation & deactivation.
 1. [ ] The Out-In interpolation is broken.   


### Refactorings

 1. [x] Move interpolators to flowutils library. 
 1. [x] Create interpolator functions.
     1. Add parameters for the interpolators where they are relevant
     1. Create a separate Range type for Interpolators.
     1. The aim is to unify interpolators and normal functions if possible.  
 1. [x] Maybe remove quad, cubic, quart and quint interpolators, or rename to pow2, pow3, pow4, pow5. 
 1. [ ] Write own layout managers and replace miglayout use with them
     * [ ] SequenceLayout - lay out components in a column or row, support alignment, grow weight, margins and padding. 
 1. [ ] Change demo editor to only repaint and revalidate UI after all editors for effects and parameters in a demo have been
    created, when changing the demo.
 1. [x] Change noise to use Vector2:s for offsets
 1. [x] Change sine and noise to use min and max output value instead of amplitude and offset
 1. [ ] Separate functions with two parameters and fields into two different Range objects, as they are conceptually different, although same in practice?
 1. [ ] Rename Range to Type or similar.
 1. [ ] Add context menu support for nodes
 1. [x] Change XML format to include value field in parameter tag as an attribute if it is non-null and a simple string instead of an element
 1. [ ] Refactor so that common services are in a context class and pass it around, make DemoComponentManager a service?
 
 
### Features   

 1. [x] Add support for adding and removing effects.
 1. [x] Select activation and deactivation times for effects (with sliders on time view)
 1. [ ] Add support for adding and removing variables, and referencing them from enclosed trees (dynamically created parameters).
 1. [ ] Add context menu to node UIs, with cut, copy and paste (maybe have a paste buffer with last 4-8 entries or so).
    Also have paste as new variable.
 1. [ ] Implement gradient editing with values at time positions and interpolators for interpolating between them.
 1. [ ] Implement effect rearranging
 1. [ ] Add sliders to number value editors with finite ranges, and tweakers to ones with infinite ranges. 
 1. [x] Add save and load of demo as an xml file.
 1. [ ] Import demo as effect group feature
 1. [x] Main time view bar for demo, with current visible area (drag to pan, wheel to zoom), showing currently edited time and current demo progress
 1. [ ] Add support for exporting and importing individual effects or calculators as libraries.
 1. [ ] Add undo/redo support if feasible (store the edit operation, use xml format to store added or removed values)                                 
 1. [ ] Shader types
 1. [ ] Editable shaders (text area)
 1. [ ] Programmable Pixel (and vertex?) shader, converts calculators to shader code?? (only supports some variable and calculator types - double, vector2, vector3, color, int, etc.)
 1. [ ] Expression function / calculator, that parses an expression and generates calculator / function / field based on that (compile to java bytecode, or use in shader).

  
### Content  
  
 1. [ ] Add a calculator that takes a function and applies it to a parameter
 1. [ ] Add all basic common 1, 2 and 3 etc double => double math operations. (add, sub, mul, div, mod, round, floor, wrap, clamp, mix, map, ceil, min, max, pow, log, exp, fuzzy logic, etc)  
 1. [ ] Add comparison operations (1 or 2 doubles => boolean).  
 1. [ ] Add logic operations (1 or 2 booleans => boolean).  
 1. [ ] Add if calculator / function.  
 1. [x] Add 3D noise, implementing field and providing the third dimension as an additional parameter, to allow easily animated noise.
 1. [ ] Add 1D noise.
 1. [ ] Add 2D noise implementing function, and providing additional dimension as parameter, for animated 1D noise.
 1. [x] Add oscillator calculator based on oscillator function code from soundrasp project (supported large variety of waveforms IIRC).
 1. [ ] Add color gradient / function type
 1. [ ] Implement and add Worley noise (1D, 2D, 3D versions, with lots of customizable parameters).
 1. [ ] Implement splatter noise (splat some area of a field at specified coordinates with generated parameters/seed, generated using poisson sampling of a grid and combined with some interpolator).
 1. [ ] Add support for multi-octave noises.
 1. [ ] Add gradient noises.
 1. [ ] Particle system effect     
 1. [ ] Star scroller effect? (Non physical particle system?)     
 1. [ ] Triangle heightfield effect.
     * Line graphics or filled triangles.
     * A number of ecotypes/textures for each corner, blend between them based on weight.
     * [ ] Add support for a vector field that modifies the placement of the vertices in the heightfield 
 1. [ ] Grid based liquid simulation.
 1. [ ] Item distribution on heightfield.
 1. [ ] Whole screen shader effect (e.g. atmosphere, etc). 
    Load shader code from file, or multiline text field.  
 1. [ ] Recursive shape generation, for tree and other shapes.  (Skeletal, possibly animated?)
     * Use calculators to generate the shapes - add some kind of shape types.
 1. [ ] Genetic algorithm for evolving controllers for articulated skeletons / particle systems / shapes.   
 1. [ ] Real-time sound generation, based on soundrasp code?      
 1. [ ] Speech synthesis
 1. [ ] Markov chain based text generator
 1. [ ] Mixed voxel / heightfield?
       
       
### Evolving 
 
 * Could this editor be used as a game editor as well?  
     * Would change the demo to not have a fixed end, 
     * Support for configuring entity generators, 
     * editing game HUDs/menus, 
     * maybe level concept or similar.
     * Editing AIs and other in game settings could be done with (possibly nested) entities with components
     * Save format could be a bit more generic in naming.
     * Editor would not feature a timeline so centrally (except for cut-scenes maybe?)
     * Would probably need a good support for editing more tabular data, e.g:
         * Enemy / monster lists 
         * Loots / powerups
         * Game objects / items in general
         * levels
         * places / props / background items
         * All of the above are basically generators for said things, in this case generating by using a prototype.  
           Variation, randomness, provided situational parameters (e.g. terrain information when generating a tree), 
           and use of various generation functions would be useful.
         * Default keyboard bindings / controls
     
      
 
 