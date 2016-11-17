Dumb Arithmetic Calculator:
--------------------------------------
Problem Statement:

In this assignment, you are supposed to build a Dumb Arithmetic Calculator (DAC) and an accompanying Web Application. A DAC can present the User with an interface which can take inputs, but can not process them itself. Instead, the operands and operator are ferried over to an application, running on a server, which performs the requested operation on the given operands, and return the result.

For example, if the DAC sends “4.5”, “3.23” and “+” to the server, it would receive a reply “7.73”. You are free to have as many operators you wish in the app, but a minimum of four operators, namely addition, subtraction, multiplication and division, are a must.

The Web Application running on the server, on the receipt of the requests, do the computation and returns the result. However, it simulates a cache to see if a specific request had been sent to it before, and replies to the request from the stored result in the cache, instead of recomputing the same. To simulate the cache, the Web Application can make use of any persistence option, say a MySQL table, or even a simple File, and store the request triplets it receives, along with the response it sent for them.

The DAC has to be implemented as an Android App. On the server side, you are free to pick any Server Side Scripting language that you are familiar with (e.g. PHP, Java, Python, Ruby etc.), deployed over Linux environment.

/*****************/

Please see that inorder for the application to work properly, the server IP address has to be provided at line 195 in MainActivity.java file in code for android studio. Currently, the address given there is "http://dac.comli.com/finalDac.php" which need to be replaced with the IP on which main.php file has to be run.

/*****************/