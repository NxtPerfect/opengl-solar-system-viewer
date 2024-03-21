# Solar system
- [x] Sun in the center
- [ ] Camera rotates around it
- [ ] Control camera with mouse
- [ ] Zoom in/out with scroll wheel
- [x] Other planets
- [x] Planets orbiting around Sun
- [ ] Checkbox to enable orbit lines

# Planet class
- [x] render sphere
- [x] orbit method, which should rotate the planet, and move it on the orbit
- [/] shaders
	- [x] read shader from file, vertex and fragment
		- [x] currently all planets are black
		meaning the shader is loaded wrong
	- [ ] currently planets aren't correctly sized to screen
	they get squished, and all render in the center
	- [ ] each planet should have different shader
- [x] movement
	- [x] since the planet is always recreated on display, it never rotates
	- [x] how to move the element around
		- [x] this works but calculations of orbit are wrong
	- [x] how to move it around the sun
	- [x] spin the planet around it's own axis
	- [x] currently the whole scene gets moved
	while it should only move individual element