# DMS
#### E-Mailing Service in scala.

Typical json config example (gmail):
``` json
 {
   "username": "user@name.com",
   "password": "password",
 
   "properties": {
     "mail.smtp.auth": "true",
     "mail.smtp.starttls.enable": "true",
     "mail.smtp.host": "smtp.gmail.com",
     "mail.smtp.port": "587"
   },
 
   "recipients": [
     "recipient1@domain.com",
     "recipient2@domain.com"
   ],
 
   "subject" : "The Subject.",
 
   "text" : "The text.",
 
   "attachments":[
     "/path/to/file/somefile.ext",
     "/path/to/another/file/anotherfile.ext"
   ]
 }
```
