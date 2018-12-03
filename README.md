
# Anypoint Template: SAP to Salesforce Contact Migration	

<!-- Header (start) -->
This template moves a large set of contacts from SAP to Salesforce. It is triggered by an HTTP call either manually or programmatically. Contacts are upserted so that the migration can be run multiple times without worrying about creating duplicates. 

The parent accounts are also migrated if they do not already exist in Salesforce. This template uses the Mule batch module to make moving a large set of data easier and more transparent.

![1436d68f-b82d-4f6a-80a4-87e3cb9abb76-image.png](https://exchange2-file-upload-service-kprod.s3.us-east-1.amazonaws.com:443/1436d68f-b82d-4f6a-80a4-87e3cb9abb76-image.png)

[![YouTube Video](http://img.youtube.com/vi/lg-HQ8H0k7c/0.jpg)](https://www.youtube.com/watch?v=lg-HQ8H0k7c)
<!-- Header (end) -->

# License Agreement
This template is subject to the conditions of the 
<a href="https://s3.amazonaws.com/templates-examples/AnypointTemplateLicense.pdf">MuleSoft License Agreement</a>.
Review the terms of the license before downloading and using this template. You can use this template for free 
with the Mule Enterprise Edition, CloudHub, or as a trial in Anypoint Studio.

# Use Case
<!-- Use Case (start) -->
Use this template to sync contacts from SAP to Salesforce for one time synchronization by browsing to an HTTP endpoint. The inbound SAP endpoint retrieves all contacts in SAP using the standard BAPI BAPI_CUSTOMER_GETCONTACTLIST and transforms the SAP contacts to Salesforce contacts. You can choose to either create an account or contact during the migration process. This functionality relies on the standard BAPI BAPI_CUSTOMER_GETDETAIL2 to retrieve details about customers.
<!-- Use Case (end) -->

# Considerations

<!-- Considerations (start) -->
To make this template run, there are certain preconditions that must be considered.
All of them deal with the preparations in both source (SAP) and destination (Salesforce) systems, that must be made for everything to run smoothly. Failing to do so could lead to unexpected behavior of the template.

Before using this template, see [Install the SAP Connector in Studio](https://docs.mulesoft.com/connectors/sap/sap-connector#install-the-sap-connector-in-studio), that helps you work with SAP and Anypoint Studio.

## Disclaimer

This template uses private Maven dependencies from MuleSoft to work. If you intend to run this template with Maven support, you need to add extra dependencies for SAP to the pom.xml file.
<!-- Considerations (end) -->

## SAP Considerations

Here's what you need to know to get this template to work with SAP.

### As a Data Source

The SAP backend system is used as a source of data. The SAP connector is used to send and receive the data from the SAP backend. 
The connector can either use RFC calls of BAPI functions and/or IDoc messages for data exchange, and needs to be properly customized per the "Properties to Configure" section.

## Salesforce Considerations

Here's what you need to know about Salesforce to get this template to work.

- Where can I check that the field configuration for my Salesforce instance is the right one? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=checking_field_accessibility_for_a_particular_field.htm&language=en_US">Salesforce: Checking Field Accessibility for a Particular Field</a>
- Can I modify the Field Access Settings? How? See: <a href="https://help.salesforce.com/HTViewHelpDoc?id=modifying_field_access_settings.htm&language=en_US">Salesforce: Modifying Field Access Settings</a>

### As a Data Destination

There are no considerations with using Salesforce as a data destination.

# Run it!
Simple steps to get SAP to Salesforce contact migration running.
<!-- Run it (start) -->

<!-- Run it (end) -->

## Running On Premises
In this section we help you run your template on your computer.
<!-- Running on premise (start) -->

<!-- Running on premise (end) -->

### Where to Download Anypoint Studio and the Mule Runtime
If you are a newcomer to Mule, here is where to get the tools.

+ [Download Anypoint Studio](https://www.mulesoft.com/platform/studio)
+ [Download Mule runtime](https://www.mulesoft.com/lp/dl/mule-esb-enterprise)
<!-- Where to download (start) -->

<!-- Where to download (end) -->

### Importing a Template into Studio
In Studio, click the Exchange X icon in the upper left of the taskbar, log in with your
Anypoint Platform credentials, search for the template, and click **Open**.
<!-- Importing into Studio (start) -->

<!-- Importing into Studio (end) -->

### Running on Studio
After you import your template into Anypoint Studio, follow these steps to run it:

+ Locate the properties file `mule.dev.properties`, in src/main/resources.
+ Complete all the properties required as per the examples in the "Properties to Configure" section.
+ Right click the template project folder.
+ Hover your mouse over `Run as`.
+ Click `Mule Application (configure)`.
+ Inside the dialog, select Environment and set the variable `mule.env` to the value `dev`.
+ Click `Run`.
<!-- Running on Studio (start) -->
To make this template run on Anypoint Studio, see [Enabling Your Studio Project for SAP](https://docs.mulesoft.com/connectors/sap-connector#configuring-the-connector-in-studio-7)
<!-- Running on Studio (end) -->

### Running on Mule Standalone
Complete all properties in one of the property files, for example in mule.prod.properties and run your app with the corresponding environment variable. To follow the example, this is `mule.env=prod`. 


## Running on CloudHub
While creating your application on CloudHub (or you can do it later as a next step), go to Runtime Manager > Manage Application > Properties to set the environment variables listed in "Properties to Configure" as well as the **mule.env**.
<!-- Running on Cloudhub (start) -->

<!-- Running on Cloudhub (end) -->

### Deploying your Anypoint Template on CloudHub
Studio provides an easy way to deploy your template directly to CloudHub, for the specific steps to do so check this
<!-- Deploying on Cloudhub (start) -->

<!-- Deploying on Cloudhub (end) -->

## Properties to Configure
To use this template, configure properties (credentials, configurations, etc.) in the properties file or in CloudHub from Runtime Manager > Manage Application > Properties. The sections that follow list example values.
### Application Configuration
<!-- Application Configuration (start) -->
**HTTP Connector Configuration**
+ http.port `9090`

**SAP Connector Configuration**

+ sap.jco.ashost `your.sap.address.com`
+ sap.jco.user `SAP_USER`
+ sap.jco.passwd `SAP_PASS`
+ sap.jco.sysnr `14`
+ sap.jco.client `800`
+ sap.jco.lang `EN`

**SalesForce Connector Configuration**

+ sfdc.username `bob.dylan@sfdc`
+ sfdc.password `DylanPassword123`
+ sfdc.securityToken `avsfwCUl7apQs56Xq2AKi3X`

**SMTP Services Configuration**

+ smtp.host `smtp.gmail.com`
+ smtp.port `587`
+ smtp.user `gmailuser`
+ smtp.password `gmailpassword`

**Mail details**

+ mail.from `your.email@gmail.com`
+ mail.to `your.email@gmail.com`
+ mail.subject `Mail subject`

**Policy for creating accounts in SF syncAccount, doNotCreateAccount**

+ account.sync.policy `syncAccount`
<!-- Application Configuration (end) -->

# API Calls
<!-- API Calls (start) -->
Salesforce imposes limits on the number of API calls that can be made.
Therefore calculating this amount may be an important factor to
consider. Product Broadcast Template calls to the API can be
calculated using the formula:

**X * 3 + X / 200** -- Where X is the number of contacts to synchronize on each run.

Multiply by 3 because for every user if account.sync.policy is set to value **syncAccounts** for every contact is checked
if an account with matching name exists in Salesforce and if not it will be created.

Then divide by 200 because, by default, contacts are gathered in groups
of 200 for each Upsert API call in the commit step. 

For instance if 10 records are fetched from an origin instance, then 31 API
calls to Salesforce are made (worst case scenario). If accounts already exist or syncing of accounts is disabled, there will be fewer API calls made.
<!-- API Calls (end) -->

# Customize It!
This brief guide intends to give a high level idea of how this template is built and how you can change it according to your needs.
As Mule applications are based on XML files, this page describes the XML files used with this template.

More files are available such as test classes and Mule application files, but to keep it simple, we focus on these XML files:

* config.xml
* businessLogic.xml
* endpoints.xml
* errorHandling.xml

<!-- Customize it (start) -->

<!-- Customize it (end) -->

## config.xml
Configuration for connectors and configuration properties are set in this file. Even change the configuration here, all parameters that can be modified are in properties file, which is the recommended place to make your changes. However if you want to do core changes to the logic, you need to modify this file.

In the Studio visual editor, the properties are on the *Global Element* tab.
<!-- Config XML (start) -->

<!-- Config XML (end) -->

## businessLogic.xml
Functional aspect of the template is implemented in this XML file, directed by one flow that checks for Salesforce creates or updates. The several message processors constitute these high level actions that fully implement the logic of this template:
1. Migration process starts by fetching all the existing contacts that match the filter criteria from SAP.
2. Each SAP contact is checked by name against Salesforce, if there is an existing matching objects in Salesforce.
3. Accounts associated with SAP Contact are migrated to an account associated with contact in Salesforce. The matching is performed by querying a Salesforce instance for an entry with name same as the given SAP Account name.
4. Then the upsert of contact in Salesforce is performed.
5. Finally during the *On Complete* stage the template logs output statistics data into the console and send email.
<!-- Business Logic XML (start) -->

<!-- Business Logic XML (end) -->

## endpoints.xml
This file provides the inbound and outbound sides of your integration app.
This template has only an HTTP Listener as the way to trigger the use case.
### Trigger Flow
**HTTP Listener Connector** - Start Report Generation
+ `${http.port}` is set as a property to be defined either on a property file or in CloudHub environment variables.
+ The path configured by default is `migratecontacts` and you are free to change for the one you prefer.
+ The host name for all endpoints in your CloudHub configuration should be defined as `localhost`. CloudHub will then route requests from your application domain URL to the endpoint.
+ The endpoint is a *request-response* and a result of calling it is the response with the total records fetched by the criteria specified.
<!-- Endpoints XML (start) -->

<!-- Endpoints XML (end) -->

## errorHandling.xml
This file handles how your integration reacts depending on the different exceptions. 
This file provides error handling that is referenced by the main flow in the business logic.
<!-- Error Handling XML (start) -->

<!-- Error Handling XML (end) -->

<!-- Extras (start) -->

<!-- Extras (end) -->
