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

FIXME


## License

Copyright © 2018 Juergen Gmeiner

Distributed under the MIT License.
