# SimulatorOfDyenaBoxes
Web application that emulates realistic data feed behaviour and blackout events of multiple Dyena boxes. It has a web interface allowing a user to control the simulation
process. 

A Dyena box is a device recording vessel motion, location and performance parameters and sends it to a server once a second.

The application reads data recorded by Dyena boxes during vessels' past journeys, processes the data, prepares the data to be included into
an HTTP response should an HTTP request be received. This happens once a second. The data is send in JSON format.
