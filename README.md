Road Alert
=======

Road Alert is an Android application developed during the Romanian Hackathon event in 2012. It won first prize in the competition.

NOTICE
-----------

It is not currently ready for production. If you fork the repository, you'll have to use your own map keys, web service, Google Cloud Messaging settings and developer signatures.
If you want to do something using the code, please let us know so we can collaborate on it.


Building the application
-----------

In order to run the project correctly you must add a `LocalConfig.java` file in the `com.makanstudios.roadalert.utils` package containing your own custom settings:

    package com.makanstudios.roadalert.utils;

    public final class LocalConfig {

	    private LocalConfig() {
	    }

	    public static final String GOOGLE_MAPS_API_KEY_RELEASE = "";

	    public static final String GOOGLE_MAPS_API_KEY_DEBUG = "";

	    public static final int DEVELOPER_SIGNATURE_DEBUG = 0;

	    public static final int DEVELOPER_SIGNATURE_RELEASE = 0;

	    public static final String API_ENDPOINT = "0";

	    public static final String API_PATH = "";

	    public static final String GCM_SENDER_ID = "0";

	    public static final String GCM_SERVER_URL = "0";

	    public static final String GCM_NEW_NOTIFICATION_INTENT = "";

	    public static final String API_KEYSTORE_TYPE = "";

	    public static final String API_KEYSTORE_PASS = "";

	    public static final String API_USERNAME = "";

	    public static final String API_PASSWORD = "";
    }

Also you must add a xml file in the res/values folder with your maps api key and a
keystore for your web service in the res/raw folder.


Developed By
------------

Makan Studios ([site][1]) - <contact@makan-studios.com> 


Credits
------------

[Cyril Mottier][2] - Provided the Polaris Android library project


License
-------

	Copyright (C) 2012 Makan Studios (http://makan-studios.com)
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	     http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

[1]: http://makan-studios.com
[2]: http://android.cyrilmottier.com/
