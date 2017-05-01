# 350S17-15-WiDaC
**Home Page**
MainActivity.java

**Settings**

SettingsActivity.java

Also uses:

Session.java

A Session object stores all relevant data for the current session, including the composite keys of all objects that have been processed, the name of the Bluetooth device that we will connect to, and an instance of a DBConnection (Database Connection). Any sample pulled from the database using the Session's instance of the DBConnection will be recorded and used for the Session Report and Visualization.

**Search**

SearchActivity.java

Database classes:

Sample.java

A Sample is a wrapper for the data that comes from the database. The database sends JSON objects to the app, which are parsed into Sample objects that we work with. Any changes to the underlying data sent between the database and the app will have to also be reflected in the sample class. All data from the database regarding the entries is parsed into a Sample class.

DBConnection.java

A DBConnection is a high-level object that manages communication between the app and the database. It allows functionality such as posting weights to and getting samples from the database. As database calls are asynchronous, they must be passed Callbacks which define what the app should do when it receives a response from the database. An example Callback is sampleCallback at line 170 of SearchActivity.java, which updates the Activity with the retrieved sample’s information. A DBConnection uses a Retrofit client in order to generate an implementation of the WidacService. Any desire to change the underlying database the app uses will require a change not in the DBConnection but in the WidacService and Retrofit classes.

RetrofitClient.java

We use Retrofit (http://square.github.io/retrofit/) to handle connections to the database. A Retrofit client is able to generate an implementation of a WidacService. It is also responsible for the manner in which the app communicates with the database. Any changes in the manner of comunication (how it connects) between the app and the database should be done here.

WidacService.java

WidacService contains the endpoint of the HTTP API which we are querying. It also contains methods which correspond to different HTTP requests that can be made to the API, such as POST, GET, and DELETE. The WidacService is responsible for defining what database the app will be connecting to, thus any changes relating to connecting to a specific database (what database it connects) should be done here.

Bluetooth classes:

BluetoothService.java

A BluetoothService manages the connection between the app and the chosen Bluetooth device, including connecting and reconnecting. In the run() method of ConnectedThread, a private class in BluetoothService, we call the method parseBytesNutriscale of BluetoothHelper to parse the incoming bytes from the Bluetooth device and obtain the weight. This class uses the default android methods for dealing with bluetooth devices and thus shouldn't require any changes for connecting to new bluetooth devices. However, if there is a change in the number of bytes that should be read from the scale the run method of connect thread should be updated (specifically mmInStream.available() >= 2, 2 can be changed to the number of bytes to be read in order to give quicker response messages to the user)

BluetoothHelper.java

BluetoothHelper contains a method to parse the incoming bytes into a weight, using the Nutriscale protocol. This would need to be modified depending on the protocol by which the scale communicates.

**Visualization**

VisualizationActivity.java

**Session Report**

SessionReportActivity.java
  -Any changes to what metrics are reported and calculated should be done in this class. 

Also uses:

SampleStaging.java

SampleStaging aggregates all database responses made in the current session into a single set of samples, which is used to display data in the Session Report.