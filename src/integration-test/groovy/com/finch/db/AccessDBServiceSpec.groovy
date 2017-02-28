package com.finch.db

import grails.test.mixin.integration.Integration
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

@Integration
@Rollback
class AccessDBServiceSpec extends Specification {
  AccessDBService accessDBService

  def "test insert into foo table"() {
    given:
    int numFoos = accessDBService.getNumFoos()

    when:
    accessDBService.insertFoo()
    int newNumFoos = accessDBService.getNumFoos()

    then:
    newNumFoos == numFoos + 1
  }
}
