
# kotlin

## Using your PubNub keys

If you would like to run integration tests against your keys, Execute the following commands to add your publish, subscribe and secret keys to your local copy of the SDK:


```bash
cd src/test/resources/
echo pub_key=YOUR_PUB_KEY >> config.properties
echo sub_key=YOUR_SUB_KEY >> config.properties
echo pam_pub_key=YOUR_PAM_PUB_KEY >> config.properties
echo pam_sub_key=YOUR_PAM_SUB_KEY >> config.properties
echo pam_sec_key=YOUR_PAM_SEC_KEY >> config.properties
```