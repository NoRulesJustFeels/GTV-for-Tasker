Anymote-for-Tasker
==================

<p>Anymote-for-Tasker implements a plugin for the popular <a href="https://play.google.com/store/apps/details?id=net.dinglisch.android.taskerm">Tasker</a> app.
Anymote-for-Tasker allows you to create tasks to control a <a href="https://developers.google.com/tv/">Google TV</a> device.
Anymote-for-Tasker allows you to send key codes to Google TV devices like pressing the buttons on a physical remote control.</p>

<p>The Anymote-for-Tasker plugin is available in the <a href="https://play.google.com/store/apps/details?id=com.entertailion.android.tasker">Google Play Store</a> as a free app.</p>

<p>The Anymote-for-Tasker can be used with the following apps:
<ul>
<li><a href="https://play.google.com/store/apps/details?id=net.dinglisch.android.taskerm">Tasker</a></li>
<li><a href="https://play.google.com/store/apps/details?id=com.twofortyfouram.locale">Locale</a></li>
<li><a href="https://play.google.com/store/apps/details?id=com.jwsoft.nfcactionlauncher">NFC Task Launcher</a> (combined with Tasker)</li>
</ul>
</p>

<p>It is important to put delays between key codes sent to Google TV devices. These devices cannot handle a stream of incoming commands in rapid succession.
It is recommend to use at least 500ms between commands to the same device and at least 2 seconds when switching between devices.
It will require some experimentation to determine what the exact values need to be for your devices.
</p>

<p>How to create a Google TV task in Tasker:
<ul>
<li>Click on the Tasks tab</li>
<li>Click on the '+' button</li>
<li>Enter a name for the new task</li>
<li>Click on the '+' button to add a new action</li>
<li>Select the 'Plugin' category</li>
<li>Select the 'GTV Tasker' action</li>
<li>Click on the 'Edit' button to to edit the action configuration</li>
<li>Wait for the Google TV devices to be discovered</li>
<li>When the progress indicator stops, select a Google TV device from the list</li>
<li>If it is a Google TV device that hasn't been paired before, your will be prompted to enter the PIN that is displayed on the Google TV device</li>
<li>Once the device is paired, select a keycode to be send to the Google TV device</li>
<li>Click on the checkmark button to save the configuration</li>
<li>Click on the checkmark for the 'GTV Tasker' action</li>
<li>Add other tasks (remember to add 'Task/Wait' actions between each GTV actions)</li>
<li>Click on the checkmark button to save the new task</li>
</ul>
</p>

<p>How to program a NFC tag to run a Google TV task using NFC Task Launcher:
<ul>
<li>Create a task in Tasker (as described above)</li>
<li>Enable external access to tasks using 'Menu/Preferences/Misc/Allow External Access' in Tasker</li>
<li>In NFC Task Launcher, click on the '+' icon to create a new task</li>
<li>Enter a tag name</li>
<li>Click on 'Add actions'</li>
<li>Select 'Tasker' and then 'Tasker Task' from the list of actions</li>
<li>Click on the 'Next' button</li>
<li>Click on the search icon and select the Tasker task you created</li>
<li>Click on the 'OK' button</li>
<li>Click on 'SAVE & WRITE'</li>
<li>Place the NFC tag against back of phone to write the task.</li>
<li>A dialog will be displayed once the task is written to the NFC tag.</li>
</ul>
</p>

<p>Developers:
<ul>
<li>The Anymote-for-Tasker code is based on the <a href="http://www.twofortyfouram.com/developer.html">Locale Toast Setting</a> plugin source code.</li>
<li>The Anymote-for-Tasker project includes the jar file for the <a href="https://github.com/entertailion/Anymote-for-Java">Anymote-for-Java</a> library in its lib directory. 
Developers should check out that project and export a jar file for the latest version of the code. 
When you export the Anymote-for-Java project as a jar file, do not include its lib directory since this project already includes those jar files.</li>
<li>The code also relies on an Android library project that is part of the <a href="http://www.twofortyfouram.com/developer/toast.zip">Locale Toast Settings plugin</a> developed by <a href="http://www.twofortyfouram.com/developer.html">two forty four a.m.</a>.
Extract the zip file and import the 'locale-api' project into Eclipse which will be referenced by the Anymote-for-Tasker project.</li>
</ul>
</p>

<p>References:
<ul>
<li><a href="http://tasker.dinglisch.net/plugins.html">Developing Tasker plugins</a></li>
<li><a href="https://developers.google.com/tv/remote/docs/anymote">Anymote Protocol</a></li>
<li><a href="https://developers.google.com/tv/remote/docs/developing">Building Second-screen Applications for Google TV</a></li>
<li>The <a href="https://github.com/entertailion/Android-Anymote">Android-Anymote</a> app provides an Android user interface for the <a href="https://github.com/entertailion/Anymote-for-Java">Anymote-for-Java</a> library.</li>
<li>The ultimate Google TV remote: <a href="https://play.google.com/store/apps/details?id=com.entertailion.android.remote">Able Remote</a></li>
</ul>
</p>
