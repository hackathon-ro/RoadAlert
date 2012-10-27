<html>

<head>
<title>GCM Admin</title>
<link rel='icon' href='favicon.png' />
</head>

<body>
<h1>GCM for Road Alert</h1>
Choose type of gcm message!
<br />
-----------------------------------------------
<br />

<form name='form' method='POST' action='sendAll'><input
	type='radio' name='gcm_message_type' value='0' checked />Sync<br />
<input type="checkbox" name='gcm_sync_core' />Sync core<br />
<input type="checkbox" name='gcm_sync_reports' />Sync reports<br />
<input type="checkbox" name='gcm_sync_stats' />Sync stats<br />

<input type='submit' value='Send Message' /></form>
</body>

</html>
