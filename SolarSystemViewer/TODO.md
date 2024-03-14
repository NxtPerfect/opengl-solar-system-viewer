# Solar system
- [ ] Sun in the center
- [ ] Camera rotates around it
- [ ] Control camera with mouse
- [ ] Zoom in/out with scroll wheel
- [ ] Other planets
- [ ] Planets orbiting around Sun

# Planet class
- [x] render sphere
- [/] orbit method, which should rotate the planet, and move it on the orbit
	- [ ] rotate the view?
- [ ] shaders
	- [ ] pass them in somehow?
	- [ ] change the planet's color
- [/] movement
	- [x] since the planet is always recreated on display, it never rotates
	- [x] how to move the element around
		- [x] this works but calculations of orbit are wrong
	- [x] how to move it around the sun
	- [/] spin the planet around it's own axis
	- [ ] currently the whole scene gets moved
	while it should only move individual element