# 350S17-15-WiDaC
**Home Page**
MainActivity.java

**Settings**
SettingsActivity.java

Also uses:
Session.java
A Session object stores all relevant data for the current session, including the composite keys of all objects that have been processed, the name of the Bluetooth device that we will connect to, and an instance of a DBConnection (Database Connection).

**Search**
SearchActivity.java

Database classes:
Sample.java
A Sample is a wrapper for the data that comes from the database. The database sends JSON objects to the app, which are parsed into Sample objects that we work with.

DBConnection.java
A DBConnection is a high-level object that manages communication between the app and the database. It allows functionality such as posting weights to and getting samples from the database. As database calls are asynchronous, they must be passed Callbacks which define what the app should do when it receives a response from the database. An example Callback is sampleCallback at line 170 of SearchActivity.java, which updates the Activity with the retrieved sample’s information. A DBConnection uses a Retrofit client in order to generate an implementation of the WidacService.

RetrofitClient.java
We use Retrofit (http://square.github.io/retrofit/) to handle connections to the database. A Retrofit client is able to generate an implementation of a WidacService

WidacService.java
WidacService contains the endpoint of the HTTP API which we are querying. It also contains methods which correspond to different HTTP requests that can be made to the API, such as POST, GET, and DELETE.

Bluetooth classes:
BluetoothService.java
A BluetoothService manages the connection between the app and the chosen Bluetooth device, including connecting and reconnecting. In the run() method of ConnectedThread, a private class in BluetoothService, we call the method parseBytesNutriscale of BluetoothHelper to parse the incoming bytes from the Bluetooth device and obtain the weight.

BluetoothHelper.java
BluetoothHelper contains a method to parse the incoming bytes into a weight, using the Nutriscale protocol. This would need to be modified depending on the protocol by which the scale communicates.

**Visualization**
VisualizationActivity.java

**Session Report**
SessionReportActivity.java

Also uses:
SampleStaging.java
SampleStaging aggregates all database responses made in the current session into a single set of samples, which is used to display data in the Session Report.