# assets-management-digital-challenge

## How to build
  1. Make sure `javac` is in your `PATH` environment variable.
  2. At shell, browse to this directory.
  3. `gradlew build`

## How to run
  Once built: `java -jar {project root}/build/libs/assets-management-digital-challenge-0.0.1.jar`.

## Further detail
The implementation is pretty conventional. It consists of 3 layers: API, 
Business and Integration.

Given the functionality specified, the controller barely contains any logic, 
just a few annotations for input validation. On the output side, I have added 
a `@ControllerAdvice` to handle exceptions and produce a more meaningful 
(and safe) output for errors. That includes changing the default 500 status 
code by a 400 (bad request) in case of input validation errors.

The business layer consist of one class (and its interface), which contains the 
application logic. The update of the coordinates once a shop is saved is done 
in a non blocking manner. The algorithm to calculate the closest shop to the 
location is anything but efficient. With more time (and possibly some research) 
I would develop a way to limit the number of shops retrieved and therefore the 
number of distances to calculate.

The integration layer consist of two classes, one integrating the service with
Google Maps, and another implementing the data store.

I chose to implement data storage with a `ConcurrentHashMap` because the 
wording said that _elegant and simple code wins over feature rich every time_.
I considered using an in-memory RDBMS such as H2 but would have been overkill. 
I also considered using a NoSQL database but the ones I know better (Redis, 
CouchDB,...) are not trivial to embed. In my spare time I found couchbase lite
which may do the job but I have no practical experience with it and I thought 
that researching new technologies was out of the scope of the challenge.
By then I already had a sort or POC that you can see in branch `feature/couchbase`.

The integration with Google Maps is based on a client, not developed, but 
endorsed by Google. Instead of using it directly from the business service, I
chose to hide the details of its use model (API keys, client model,...) in a 
different class to decouple business logic from this external client making it
easier to replace the client (if needed) in the future.

The whole service lacks logging and javadoc (another thing to improve).

There aren't many unit tests because when I start an application from scratch, 
I prefer BDD only. My own experience tells me that in the first stages of 
development, frequent refactorings tend to render many unit tests invalid, 
due to changes in method signatures. Functional and integration tests, have a
longer life expectancy.

I've also added a SoapUI project which contains request examples (see 
`tests/assets-management-digital-challenge-soapui-project.xml`).
