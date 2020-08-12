# kotlin

## Using your PubNub keys

If you would like to run integration tests against your keys, Execute the following commands to add your publish, subscribe and secret keys to your local copy of the SDK. 

```bash
cd src/test/resources/
echo pub_key=YOUR_PUB_KEY >> config.properties
echo sub_key=YOUR_SUB_KEY >> config.properties
echo pam_pub_key=YOUR_PAM_PUB_KEY >> config.properties
echo pam_sub_key=YOUR_PAM_SUB_KEY >> config.properties
echo pam_sec_key=YOUR_PAM_SEC_KEY >> config.properties
```

Please create two keys:
- with disabled Access Manager - pub, sub
- with enabled Access Manager - pam_pub, pam_sub, pam_sec

Configuration of the rest functionality should be:

|               Key                |            Value            |
| -------------------------------- | --------------------------- |
|           **PRESENCE**           |           **ON**            |
|           Announce Max           |             20              |
|             Interval             |             30              |
|         Presence Deltas          |             OFF             |
| Generate Leave on TCP FIN or RST |             ON              |
|         Global Here Now          |             ON              |
|         Stream Filtering         |             ON              |
|             Debounce             |              2              |
|      **STORAGE & PLAYBACK**      |           **ON**            |
|             Retention            |            7 Day            |
|    Enable Delete-From-History    |             ON              |
|      Include presence events     |             ON              |
|       **STREAM CONTROLLER**      |                             |
|    Enable Wildcard Subscribe     |             ON              |
|            **OBJECTS**           |                             |
|              Region              |      select nearest one     |
|       User Metadata Events       |             ON              |
|     Channel Metadata Events      |             ON              |
|         Membership Events        |             ON              |
|       **PUBNUB FUNCTIONS**       |             ON              |
|        **ACCESS MANAGER**        |   ON for PAM, OFF default   |
