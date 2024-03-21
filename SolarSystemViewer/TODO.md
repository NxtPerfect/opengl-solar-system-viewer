# Solar system
- [x] Sun in the center
- [ ] Camera rotates around it
- [ ] Control camera with mouse
- [ ] Zoom in/out with scroll wheel
- [x] Other planets
- [x] Planets orbiting around Sun
- [ ] Checkbox to enable planet names
- [ ] Checkbox to enable orbit lines
- [ ] Render text

# Planet class
- [x] render sphere
- [/] orbit method, which should rotate the planet, and move it on the orbit
	- [ ] rotate the view?
- [/] shaders
	- [/] read shader from file, vertex and fragment
		- [ ] currently all planets are black
		meaning the shader is loaded wrong
	- [ ] pass them in somehow?
	- [ ] change the planet's color
- [x] movement
	- [x] since the planet is always recreated on display, it never rotates
	- [x] how to move the element around
		- [x] this works but calculations of orbit are wrong
	- [x] how to move it around the sun
	- [x] spin the planet around it's own axis
	- [x] currently the whole scene gets moved
	while it should only move individual element