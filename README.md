# grails-rollback-issue
The grails documentation (http://docs.grails.org/latest/guide/testing.html#integrationTesting) claims that the spring (org.springframework.test.annotation.Rollback) @Rollback annotation  can be used to rollback transactions on integration tests, including the database modifications made in the setup() method of the test class. This is presented in the documentation as a way to get around the limitation of the grails (grails.transaction.Rollback) @Rollback annotation that will rollback database changes made in a test method, but will not rollback changes to the database made in the setup() method. However, it appears that the spring @Rollback annotation on a grails test doesn't do anything--it doesn't even cause changes made in the test method to be rolled back, let alone roll back any database changes made in the setup() method.

To reproduce the issue by running the test in this grails project, you need to frist create this table (this example is using a postgres database):

    CREATE TABLE foo
    (
      bar integer
    )

Open an interactive sql window, and execute the query:

    select count(*) from foo
    
You should see that there are 0 rows in the foo table since it should be empty. Next, run the AccessDBServiceSpec integration test with the following command:

    grails dev test-app com.finch.db.AccessDBServiceSpec -integration -clean

Since the test class is annotated with the spring @Rollback annotation, we expect zero rows to be in the foo table after running the test because the insert into the foo table should be rolled back when the test completes. However, running the sql query again:

    select count(*) from foo

shows that the table has a row in it. This means that the @Rollback annotation on the test class did not cause the transaction to be rolled back as expected. This appears to be a bug.

If the @Rollback annotation in AccessDBServiceSpec is changed to be the grails @Rollback annotation (grails.transaction.Rollback) instead of the spring @Rollback annotation, then the transaction does roll back correctly. However, switching to the grails @Rollback annotation isn't a solution here because what we really want to do is to rollback changes made in a setup() method as well as the test method. The grails @Rollback annotation is not capable of doing this, but according to the grails documentation, the spring @Rollback annotation should rollback changes in a setup method. As this project shows, though, using the spring @Rollback annotation appears to do absolutely nothing right now.
