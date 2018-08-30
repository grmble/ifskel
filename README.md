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
    cp target/public/cljs-out/dev-main.js cljs-resources/public/cljs-out/
    lein with-profile production immutant war -o C:\users\gj\wildfly-13.0.0.Final

XXX make this easier (lein shell && lein do)

## License

Copyright © 2018 Juergen Gmeiner

Distributed under the MIT License.
