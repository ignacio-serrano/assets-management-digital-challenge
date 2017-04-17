# assets-management-digital-challenge

## How to build
  1. Make sure `javac` is in your `PATH` environment variable.
  2. At shell, browse to this directory.
  3. `gradlew build`

## How to run
  Once built: `java -jar {project root}/build/libs/assets-management-digital-challenge-0.0.1.jar`.

## Further detail
I chose to implement data storage with a `ConcurrentHashMap` 
because the wording said that _elegant and simple code wins over feature rich every time_.
I considered using an in-memory RDBMS such as H2 but would have been overkill. 
I also considered using a NoSQL database but the ones I know better (Redis, 
CouchDB,...) are not trivial to embed. In my spare time I found couchbase lite
which may do the job but I have no practical experience with it and I thought 
that researching new technologies was out of the scope of the challenge.
By then I already had a sort or POC that you can see in branch `feature/couchbase`.

The algorithm to calculate the closest shop to the location is anything but efficient.
With more time (and possibly some research) I would develop a way to limit the number
of shops retrieved and therefore the number of distances to calculate.

The service lacks logging and javadoc (another thing to improve).

There aren't many unit tests because when I start an application from scratch, I prefer
BDD only. You don't have to delete so many tests due to refactorings.
