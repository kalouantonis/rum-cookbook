# File Upload

This recipe demostrates how to build a file upload form that allows multiple
file uploads while also displaying an upload queue.

## Overview

This component works by using the `rum/local` mixin to instiate local state 
containing a set of files. This state is passed to `file-list` that renders
the list of files as a table and provides the ability to remove unwanted files
and it also to `file-form` that creates a form and manages the files added to it.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 
