# ifskel

Immutant-Figwheel-SKELeton

A skeleton app using immutant, figwheel-main and reagent.


## Installation

Clone the git repo at https://github.com/grmble/ifskel

## Usage

#In development

Start a server repl using `lein repl`, start the system
using `(start)`.

In a different shell, start the frontend server
using `lein fig`.

    lein repl
    (start)
    
    # in a different shell
    lein fig

On the server side, the dev namespace has the functions 
`(start)` and `(stop)` that will start all server 
components (including an embedded postgres database)
and stop them again.

In development mode, changed code will be automatically reloaded.
Except sometimes you need to stop and restart the web server.
This can be done using `(web/run-dmc ifskel.core/app)`
and `(web/stop)`.

#Testing

FIXME

#Deployment

##Configuring Wildfly

Start your wildfly instance and deploy the postgresql JDBC driver.
To do this, start `jboss-cli` and do a

    deploy C:\Users\gj\.m2\repository\org\postgresql\postgresql\42.2.4\postgresql-42.2.4.jar

Next, using HAL, configure the datasource under the name `PostgresDS`

##Build a war file and deploy it

To build and deploy

    lein clean
    lein fig:min
    lein js:cp
    lein war:cp -o c:\users\gj\wildfly-13.0.0.Final

The directory name can be configured as :immutant :war :destination,
If you provide this in an :immutant profile in your profiles.clj,
you do not have to specify the directory every time.

There is a handly alias that executes all the steps from above:

    lein war:all

XXX running migrations does not work in deployed mode.
Ragtime can not find the resources ... in particular,
it can't list the directory content - classloader bullshit.

Current recommendation: point the repl at the prod db and run the
migrations.  Copy the url starting with // from your standalone.xml.


    lein repl
    (System/setProperty "db.name" "//192.168.56.3/postgres?user=postgres&password=postgres")
    (db/run-migrations)


## License

Copyright © 2018 Juergen Gmeiner

Distributed under the MIT License.
