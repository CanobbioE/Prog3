# Programming III

Implementing an email service using [RMI](https://en.wikipedia.org/wiki/Java_remote_method_invocation).
The client uses the MVC pattern.
The application uses fasterxml/jackson library to write and read json files. Most implementation choices were mandatory (such as the absence of login).

List of features:  
- emails are also saved locally client-side, that grants the ability to read old mails while offline.
- every operation is managed so there are no race conditons.
- the client can read, send, delete, reply, reply all and forward emails.
- every possible error is handled so that the application doesn't crash but reports the error to the user.
- reciving a new message is reported through pop-up.
- failing to delivery a message is reported through an error email.
- email are ordered by date.
- the server prints logs of every significant error to help debugging.
- the client is based on the MVC pattern.
