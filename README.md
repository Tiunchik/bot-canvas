# Bot-Canvas

## Project Settings
- JDK: Bellsoft Liberica (Full) 21.0.5

## Architecture 
**MVVM**

## Packages naming policy 
**Feature-based**

## Developers
- Daniils Loputevs
- Maksim Tiunchik


## TODO
- ~~Node~~
- - ~~create~~
- - ~~draw~~
- - ~~edit~~
- - ~~delete~~
- - - ~~fix~~
- ~~Link~~
- - ~~create~~
- - ~~draw~~
- - ~~delete~~
- fix map zoom
- fix map drag
- 
- undo & redo business commands
- Node editor (right sidebar)
- NodeFactory (left sidebar) 
- - serialize IR schema for create Node supplier buttons
- - sidebar with Node supplier buttons
- - (de)serialize graphs with NodeType
- switch between graphs
- switch between users
- OperationResult pattern & Show error on UI like notification
- - Status | sealed classes
- - - IN_PROCESS (LOADING | SENDING)
- - - ERROR(msg | exception)
- - - SUCCESS(payload)
- - - EMPTY